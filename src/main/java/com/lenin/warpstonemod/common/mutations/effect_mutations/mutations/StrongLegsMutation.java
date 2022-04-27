package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class StrongLegsMutation extends Mutation {
	public StrongLegsMutation(ResourceLocation _key) {
		super(_key);
	}

	/**This mutation makes the player leap forward every time they jump while sprinting
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingJump);
	}

	/**The method here is to fire a knockback whenever the player jumps while sprinting,
	 * this creates a big leap forward, the vectors have to be reveresed otherwise the
	 * player will leap backwards
	 */

	public void onLivingJump (LivingEvent.LivingJumpEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
		) return;

		if (event.getEntityLiving().isSprinting()) {
			Vector3d v = event.getEntityLiving().getLookVec();
			event.getEntityLiving().applyKnockback(0.7f, -v.x, -v.z);
		}
	}
}