package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class StrongLegsMutation extends EffectMutation {
	public StrongLegsMutation() {
		super(
                "strong_legs",
				"88a8026d-7ce3-4a21-8436-f3cce8840080"
		);
	}

	/**This mutation makes the player leap forward every time they jump while sprinting
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingJump);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	/**The method here is to fire a knockback whenever the player jumps while sprinting,
	 * this creates a big leap forward, the vectors have to be reveresed otherwise the
	 * player will leap backwards
	 */

	public void onLivingJump (LivingEvent.LivingJumpEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		if (event.getEntityLiving().isSprinting()) {
			Vector3d v = event.getEntityLiving().getLookVec();
			event.getEntityLiving().applyKnockback(0.7f, -v.x, -v.z);
		}
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.FINS) && !manager.containsEffect(EffectMutations.WEAK_LEGS);
	}
}