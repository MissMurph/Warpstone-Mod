package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.items.WarpItems;
import com.lenin.warpstonemod.common.items.WarpstoneShard;
import com.lenin.warpstonemod.common.mutations.EffectsMap;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationRegistry;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationTickHelper;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class CommonProxy {

	public static MinecraftServer SERVER;
	private TickManager tickManager;

	public void init (){
		//System.out.println("WARPLOG: Initializing CommonProxy");
		Registration.register();
		MutateHelper.init();
		//EffectsMap.init();
		EffectMutationRegistry.init();

		this.tickManager = new TickManager();
		this.attachTickListeners(tickManager::register);

		//System.out.println("WARPLOG: Initialized Global Classes");

		PacketHandler.registerPackets();

		//System.out.println("WARPLOG: Completed Initializing CommonProxy");
	}

	public void attachListeners (IEventBus bus) {
		//System.out.println("WARPLOG: Attaching Listeners in Common");

		bus.addListener(this::onServerSave);

		bus.addListener(this::onPlayerConnect);
		bus.addListener(this::onPlayerDisconnect);

		bus.addListener(this::onPlayerItemUse);
		bus.addListener(this::onPlayerDeath);

		tickManager.attachListeners(bus);
	}

	public void attachTickListeners (Consumer<ITickHandler> handler) {
		handler.accept(MutationTickHelper.INSTANCE);
	}

	public void onPlayerConnect (PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		//System.out.println("WARPLOG: Playing Logging in Event");

		MutateHelper.loadMutData(player.getUniqueID());
	}

	public void onPlayerDisconnect (PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		//System.out.println("WARPLOG: Playing Logging out Event");

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

			if (m != null) m.resetMutations(event);
		}
	}

	public static void register () {}

	public static LivingEntity getPlayerByUUID(UUID playerUUID) {
		return SERVER.getPlayerList().getPlayerByUUID(playerUUID);
	}

	public static LogicalSide getSide (UUID playerUUID) {
		return getSide(getPlayerByUUID(playerUUID));
	}

	public static LogicalSide getSide (Entity entity) {
		return entity.getEntityWorld().isRemote() ? LogicalSide.CLIENT : LogicalSide.SERVER;
	}

	public File getWarpServerDataDirectory () {
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

		if (server == null) { System.out.println("Server Not Found"); return null; }

		File dir = server.func_240776_a_(new FolderName(WarpstoneMain.MOD_ID)).toFile();

		if (!dir.exists()) dir.mkdirs();

		return dir;
	}
}