package com.lenin.warpstonemod.common.network;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.FlightMutation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FlightMutationPacket extends WarpPacket<FlightMutationPacket> {
	private boolean flying;
	private UUID uuid;

	public FlightMutationPacket() {}

	public FlightMutationPacket (UUID playerUUID, Boolean bool) {
		uuid = playerUUID;
		flying = bool;
	}

	@Nonnull
	@Override
	public Encoder<FlightMutationPacket> encoder() {
		return (packet, buffer) -> {
			ByteBufUtils.writeBool(buffer, packet.flying);
		};
	}

	@Nonnull
	@Override
	public Decoder<FlightMutationPacket> decoder() {
		return buffer -> {
			FlightMutationPacket pkt = new FlightMutationPacket();
			pkt.flying = ByteBufUtils.readBool(buffer);

			return pkt;
		};
	}

	@Nonnull
	@Override
	public Handler<FlightMutationPacket> handler() {
		return new Handler<FlightMutationPacket>() {
			@Override
			public void handle(FlightMutationPacket packet, NetworkEvent.Context context, LogicalSide side) {
				if (side.isClient()) return;

				context.enqueueWork(() -> {
					//((FlightMutation) WarpstoneMain.getEffectsMap().getEffectMutation(EffectMutations.FLIGHT))
					//		.tryFallFlying(context.getSender().getUniqueID(), packet.flying);
				});
			}
		};
	}
}