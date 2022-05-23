package com.lenin.warpstonemod.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class WSPacket<T extends WSPacket<T>> {

	@Nonnull
	public abstract Encoder<T> encoder();

	@Nonnull
	public abstract Decoder<T> decoder();

	@Nonnull
	public abstract Handler<T> handler();

	public interface Encoder<T extends WSPacket<T>> extends BiConsumer<T, PacketBuffer> {}

	public interface Decoder<T extends WSPacket<T>> extends Function<PacketBuffer, T> {}

	public interface Handler<T extends WSPacket<T>> extends BiConsumer<T, Supplier<NetworkEvent.Context>> {

		@Override
		default void accept(T t, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			switch (context.getDirection().getReceptionSide()) {
				case CLIENT:
					handleClient(t, context);
					break;
				case SERVER:
					handleServer(t, context);
					break;
			}
			context.setPacketHandled(true);
		}

		@OnlyIn(Dist.CLIENT)
		default void handleClient (T packet, NetworkEvent.Context context) {
			this.handle(packet, context, LogicalSide.CLIENT);
		}

		default void handleServer (T packet, NetworkEvent.Context context) {
			this.handle(packet, context, LogicalSide.SERVER);
		}

		void handle(T packet, NetworkEvent.Context context, LogicalSide side);
	}
}