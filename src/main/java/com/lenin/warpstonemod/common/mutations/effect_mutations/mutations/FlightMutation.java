package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.network.FlightMutationPacket;
import com.lenin.warpstonemod.common.network.PacketHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Rarity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlightMutation extends EffectMutation implements IMutationTick {
	public FlightMutation(int _id) {
		super(_id,
				"flying",
				"f9a7e9b8-3d9b-4e0e-ac28-8bbacb4e92dc",
				Rarity.EPIC);
	}

	private Map<UUID, Integer> counterMap = new HashMap<>();

	private int counter;
	private boolean flag;

	@Override
	public void attachListeners(IEventBus bus) {
		//bus.addListener(this::onLivingJump);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
 		if (side == LogicalSide.SERVER) {
			if (!player.isOnGround()) counter--;
			else {
				counter = 7;
				player.stopFallFlying();
				flag = false;
			}

			if (flag) ;

			return;
		}

		ClientPlayerEntity client = (ClientPlayerEntity) player;

		if (client.movementInput.jump) {
			FlightMutationPacket pkt = new FlightMutationPacket(client.getUniqueID(), true);
			PacketHandler.CHANNEL.sendToServer(pkt);
		}
	}

	/*public void onLivingJump (LivingEvent.LivingJumpEvent event) {
		if (event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)) return;

		if (!event.getEntityLiving().isOnGround()) {
			((PlayerEntity)event.getEntityLiving()).startFallFlying();
		}
	}*/

	public void tryFallFlying (UUID playerUUID, boolean flag){
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		PlayerEntity player = server.getPlayerList().getPlayerByUUID(playerUUID);

		if (flag && player != null && counter <= 0 && !player.isElytraFlying() && !player.isOnGround() && !player.isInWater() && !player.isInLava()) {
			player.startFallFlying();
			flag = true;
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager);
	}
}