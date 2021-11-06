package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

public class FinsMutation extends EffectMutation {
	public FinsMutation(int _id) {
		super(_id,
				"fins",
				"26d0153a-08da-4c90-9287-44f1e6920e7d",
				Rarity.COMMON);
	}

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote) return;

		entity.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				1f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote) return;

		entity.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.STRONG_LEGS);
	}
}