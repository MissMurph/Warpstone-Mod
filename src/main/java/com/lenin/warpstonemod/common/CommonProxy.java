package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.api.JSONBuffer;
import com.lenin.warpstonemod.api.WarpstoneAPI;
import com.lenin.warpstonemod.common.commands.WarpstoneCommand;
import com.lenin.warpstonemod.common.items.MutateItem;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.conditions.MutationConditions;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationTickHelper;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.RegisterCommandsEvent;
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
	protected Registration registration;
	//protected DataGenerators dataGen;
	private WarpstoneAPI api;

	private final JSONBuffer jsonBuffer = new JSONBuffer(Warpstone.MOD_ID);

	public void init (){
		MutateHelper.init();
		MutationTags.init();
		WSAttributes.register();
		MutationConditions.init();

		this.registration = new Registration();
		this.tickHandlers.add(MutationTickHelper.INSTANCE);

		api = WarpstoneAPI.init(this, jsonBuffer, registration);

		PacketHandler.registerPackets();
	}

	public void attachListeners (IEventBus bus) {
		bus.addListener(this::onServerSave);

		bus.addListener(this::onPlayerConnect);
		bus.addListener(this::onPlayerDisconnect);

		bus.addListener(this::onPlayerItemUse);
		bus.addListener(this::onPlayerDeath);

		bus.addListener(this::onPlayerTick);

		bus.addListener(this::onRegisterCommands);

		bus.addListener(EventPriority.HIGH, WarpstoneWorldGen::onBiomeLoading);

		jsonBuffer.attachListener(bus);
	}

	public void attachLifeCycle (IEventBus bus) {
		bus.addListener(this::onCommonSetup);
		registration.attachListeners(bus);
		jsonBuffer.attachLifecycle(bus);

		bus.addListener(Registration::onRegistryBuild);
	}

	private void onPlayerTick (TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		for (ITickHandler handler : tickHandlers) {
			handler.onTick(TickEvent.Type.PLAYER, event.player, event.side);
		}
	}

	private void onCommonSetup (FMLCommonSetupEvent event) {
		WarpstoneWorldGen.init();
	}

	private void onPlayerConnect (PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.loadPlayerData(player.getUniqueID());
	}

	private void onPlayerDisconnect (PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.unloadManager(player.getUniqueID());
	}

	private void onServerSave (WorldEvent.Save event){
		for (PlayerManager m : MutateHelper.getManagers()) {
			MutateHelper.savePlayerData(m.getParentEntity().getUniqueID());
		}
	}

	private void onPlayerItemUse (PlayerInteractEvent.RightClickItem event){
		if (event.getItemStack().getItem() instanceof MutateItem) {
			MutateItem item = (MutateItem) event.getItemStack().getItem();
			/*if (!item.canBeConsumed()) {
				event.setCanceled(true);
			}*/
		}
	}

	private void onPlayerDeath (LivingDeathEvent event){
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity p = (PlayerEntity) event.getEntityLiving();

			PlayerManager m = MutateHelper.getManager(p);

			if (m != null) m.resetMutations(true);
		}
	}

	private void onRegisterCommands (RegisterCommandsEvent event) {
		WarpstoneCommand.register(event.getDispatcher());
	}

	public File getWarpServerDataDirectory () {
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

		if (server == null) { System.out.println("Server Not Found"); return null; }

		File dir = server.func_240776_a_(new FolderName(Warpstone.MOD_ID)).toFile();

		if (!dir.exists()) dir.mkdirs();

		return dir;
	}

	public Registration getRegistration(){
		return this.registration;
	}

	public JSONBuffer getBuffer () {
		return this.jsonBuffer;
	}
}