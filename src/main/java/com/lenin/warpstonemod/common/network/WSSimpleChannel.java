package com.lenin.warpstonemod.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class WSSimpleChannel {

	private final SimpleChannel channel;

	public WSSimpleChannel(SimpleChannel _channel){
		channel = _channel;
	}

	public <P extends WSPacket<P>> void sendToPlayer (PlayerEntity player, P packet) {
		if (player instanceof ServerPlayerEntity) {
			this.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
		}
	}

	public <P extends WSPacket<P>> void sendToServer (P packet) {
		channel.sendToServer(packet);
	}

	public <MSG> void send (PacketDistributor.PacketTarget target, MSG message){
		channel.send(target, message);
	}

	public <MSG> SimpleChannel.MessageBuilder<MSG> messageBuilder (final Class<MSG> type, int id){
		return channel.messageBuilder(type, id);
	}
}