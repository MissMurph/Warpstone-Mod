package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.io.File;
import java.util.UUID;

public class CommonProxy {

	public static MinecraftServer SERVER;

	public static void init (){
		PacketHandler.registerPackets();
	}

	public static LivingEntity getPlayerByUUID(UUID playerUUID) {
		return SERVER.getPlayerList().getPlayerByUUID(playerUUID);
	}

	public void attachListeners (IEventBus bus) {
		bus.addListener(this::onServerStarted);

		bus.addListener(this::onPlayerConnect);
		bus.addListener(this::onPlayerDisconnect);
	}

	@SubscribeEvent
	public void onPlayerConnect (PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.loadMutData(player.getUniqueID());
	}

	@SubscribeEvent
	public void onPlayerDisconnect (PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		MutateHelper.savePlayerData(player.getUniqueID());
	}

	@SubscribeEvent
	public void onServerStarted (FMLServerStartedEvent event){
		SERVER = event.getServer();
	}

	public File getWarpServerDataDirectory () {
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);

		if (server == null) { System.out.println("Server Not Found"); return null; }

		File dir = server.func_240776_a_(new FolderName(WarpstoneMain.MOD_ID)).toFile();

		if (!dir.exists()) dir.mkdirs();

		return dir;
	}
}