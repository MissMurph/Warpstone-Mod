package com.lenin.warpstonemod.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;

import java.util.function.Supplier;

public class PacketHandler {

	private static int packetIndex = 0;
	private static final String PROTOCOL_VERSION = "1";

	public static final WarpSimpleChannel CHANNEL = new WarpSimpleChannel(NetworkRegistry.newSimpleChannel(
			new ResourceLocation("warpstonemod", "net_channel"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	));

	public static void registerPackets () {
		registerMessage(SyncMutDataPacket::new);
	}

	private static <T extends WarpPacket<T>> void registerMessage (Supplier<T> suppler){
		T packet = suppler.get();
		CHANNEL.messageBuilder((Class<T>) packet.getClass(), packetIndex++)
		.encoder(packet.encoder())
		.decoder(packet.decoder())
		.consumer(packet.handler())
		.add();
	}
}