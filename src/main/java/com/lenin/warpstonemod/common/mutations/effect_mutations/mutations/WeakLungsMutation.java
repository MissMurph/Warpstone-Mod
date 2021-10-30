package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

public class WeakLungsMutation extends EffectMutation {
	public WeakLungsMutation(int _id, String _mutName, String _uuid, Rarity _rarity) {
		super(_id, _mutName, _uuid, _rarity);
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
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.GILLS);
	}
}