package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.entity.LivingEntity;

public class EffectMutation extends Mutation {
	public EffectMutation(LivingEntity _parentPlayer, String _name, String _uuid) {
		super(_parentPlayer, _name, _uuid);
	}

	protected void applyMutation (){
		clearMutation();
	}

	@Override
	public void changeLevel(int value) {
		mutationLevel = value;

		if (mutationLevel > 1) mutationLevel = 1;
		if (mutationLevel < -1) mutationLevel = -1;

		applyMutation();
	}

	@Override
	public void setLevel(int value) {
		mutationLevel = value;

		if (mutationLevel > 1) mutationLevel = 1;
		if (mutationLevel < -1) mutationLevel = -1;

		applyMutation();
	}

	@Override
	public void clearMutation() {

	}

	@Override
	public int getMutationLevel() {
		return super.getMutationLevel();
	}

	@Override
	public String getMutationType() {
		return super.getMutationType();
	}
}