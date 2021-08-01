package com.lenin.warpstonemod.common;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.MutationTickHelper;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.io.File;
import java.util.UUID;
import java.util.function.Consumer;

public class CommonProxy {

	public static MinecraftServer SERVER;

	public void init (){
		System.out.println("WARPLOG: Initializing CommonProxy");
		PacketHandler.registerPackets();
		attachListeners(MinecraftForge.EVENT_BUS);
	}

	public void attachListeners (IEventBus bus) {
		System.out.println("WARPLOG: Attaching Listeners in Common");

		bus.addListener(this::onServerStarted);
		bus.addListener(this::onServerSave);

		bus.addListener(this::onPlayerConnect);
		bus.addListener(this::onPlayerDisconnect);

		bus.addListener(this::onTick);
	}

	public void onTick(TickEvent.PlayerTickEvent event){
		if (event.phase == TickEvent.Phase.START) MutationTickHelper.onTick(event);
	}

	public void onPlayerConnect (PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		System.out.println("WARPLOG: Playing Logging in Event");

		MutateHelper.loadMutData(player.getUniqueID());
	}

	public void onPlayerDisconnect (PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

		System.out.println("WARPLOG: Playing Logging out Event");

		MutateHelper.savePlayerData(player.getUniqueID());
		MutateHelper.clearManager(player.getUniqueID());
	}

	public void onServerStarted (FMLServerStartedEvent event){
		SERVER = event.getServer();
	}

	public void onServerSave (WorldEvent.Save event){
		for (MutateManager m : MutateHelper.getManagers()) {
			//MutateHelper.savePlayerData(m.getParentEntity().getUniqueID());
		}
	}

	public <T extends Event> void registerEventListener (Consumer<T> suppler) {
		MinecraftForge.EVENT_BUS.addListener(suppler);
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