package com.lenin.warpstonemod.common.network;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;

public class SyncPlayerDataPacket extends WarpPacket<SyncPlayerDataPacket> {

	private CompoundNBT data;

	public SyncPlayerDataPacket() {}

	public SyncPlayerDataPacket(CompoundNBT _data) {
		data = _data;
	}

	public CompoundNBT getData () {
		return data;
	}

	@Nonnull
	@Override
	public Encoder<SyncPlayerDataPacket> encoder() {
		return (packet, buffer) -> ByteBufUtils.writeNBT(buffer, packet.data);
	}

	@Nonnull
	@Override
	public Decoder<SyncPlayerDataPacket> decoder() {
		return buffer -> {
			SyncPlayerDataPacket pkt = new SyncPlayerDataPacket();
			pkt.data = ByteBufUtils.readNBT(buffer);

			return pkt;
		};
	}

	@Nonnull
	@Override
	public Handler<SyncPlayerDataPacket> handler() {
		return new Handler<SyncPlayerDataPacket>() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public void handleClient(SyncPlayerDataPacket packet, NetworkEvent.Context context) {
				context.enqueueWork(() -> {
					PlayerEntity p = Minecraft.getInstance().player;
					if (p != null) {
						MutateHelper.updateClientMutations(packet);
					}
				});
			}

			@Override
			public void handle(SyncPlayerDataPacket packet, NetworkEvent.Context context, LogicalSide side) {}
		};
	}
}