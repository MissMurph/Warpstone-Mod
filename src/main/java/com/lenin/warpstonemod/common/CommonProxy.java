package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationTickHelper;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommonProxy {
	private final List<ITickHandler> tickHandlers = new ArrayList<>();

	public void init (){
		Registration.init();
		MutateHelper.init();
		EffectMutations.init();

		this.tickHandlers.add(MutationTickHelper.INSTANCE);

		PacketHandler.registerPackets();
	}

	public void attachListeners (IEventBus bus) {
		bus.addListener(this::onServerSave);

		bus.addListener(this::onPlayerConnect);
		bus.addListener(this::onPlayerDisconnect);

		bus.addListener(this::onPlayerItemUse);
		bus.addListener(this::onPlayerDeath);

		bus.addListener(this::onPlayerTick);

		bus.addListener(EventPriority.HIGH, WarpstoneWorldGen::onBiomeLoading);
	}

	public void attachLifeCycle (IEventBus bus) {
		bus.addListener(this::onCommonSetup);
	}

	private void onPlayerTick (TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		for (ITickHandler handler : tickHandlers) {
			handler.onTick(TickEvent.Type.PLAYER, event.player, event.side);
		}
	}

	public void onCommonSetup (FMLCommonSetupEvent event) {
		WarpstoneWorldGen.init();
	}

	public void onPlayerConnect (PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.loadMutData(player.getUniqueID());
	}

	public void onPlayerDisconnect (PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.unloadManager(player.getUniqueID());
	}

	public void onServerSave (WorldEvent.Save event){
		for (MutateManager m : MutateHelper.getManagers()) {
			MutateHelper.savePlayerData(m.getParentEntity().getUniqueID());
		}
	}

	public void onPlayerItemUse (PlayerInteractEvent.RightClickItem event){
		if (event.getItemStack().getItem() instanceof IWarpstoneConsumable) {
			IWarpstoneConsumable item = (IWarpstoneConsumable) event.getItemStack().getItem();
			if (!item.canBeConsumed()) {
				event.setCanceled(true);
			}
		}
	}

	public void onPlayerDeath (LivingDeathEvent event){
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity p = (PlayerEntity) event.getEntityLiving();

			MutateManager m = MutateHelper.getManager(p);

			if (m != null) m.resetMutations(true);
		}
	}

	public File getWarpServerDataDirectory () {
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

		if (server == null) { System.out.println("Server Not Found"); return null; }

		File dir = server.func_240776_a_(new FolderName(WarpstoneMain.MOD_ID)).toFile();

		if (!dir.exists()) dir.mkdirs();

		return dir;
	}
}