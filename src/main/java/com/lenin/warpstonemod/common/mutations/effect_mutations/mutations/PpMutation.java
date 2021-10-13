package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.IEventBus;

public class PpMutation extends EffectMutation {
	protected PpMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.largepp",
				WarpMutations.nameConst + "effect.smallpp",
				"pp_icon.png",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19");
	}

	@Override
	public void attachListeners(IEventBus bus) {	}

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
}