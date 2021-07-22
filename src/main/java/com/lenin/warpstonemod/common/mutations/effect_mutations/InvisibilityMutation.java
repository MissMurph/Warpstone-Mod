package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class InvisibilityMutation extends EffectMutation{
	public InvisibilityMutation(LivingEntity _parentPlayer, String _name, String _uuid) {
		super(_parentPlayer, _name, _uuid);
	}

	@Override
	protected void applyMutation() {
		super.applyMutation();

		switch (mutationLevel) {
			case -1:
				parentPlayer.setGlowing(true);
				break;
			case 0:
				break;
			case 1:
				parentPlayer.setInvisible(true);
				break;
		}
	}

	@Override
	public void clearMutation() {
		super.clearMutation();

		parentPlayer.setInvisible(false);
		parentPlayer.setGlowing(false);
	}
}