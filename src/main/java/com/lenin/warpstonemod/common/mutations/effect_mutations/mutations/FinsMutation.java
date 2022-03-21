package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;

public class FinsMutation extends EffectMutation {
	public FinsMutation() {
		super(
                "fins",
				"26d0153a-08da-4c90-9287-44f1e6920e7d"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.world.isRemote) return;

		manager.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				1f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.world.isRemote) return;

		manager.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.STRONG_LEGS);
	}
}