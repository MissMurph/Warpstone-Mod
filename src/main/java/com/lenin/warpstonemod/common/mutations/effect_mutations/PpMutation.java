package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

public class PpMutation extends EffectMutation{
	public PpMutation(LivingEntity _parentPlayer, String _name, String _uuid) {
		super(_parentPlayer, _name, _uuid);
	}

	@Override
	protected void applyMutation() {}

	@Override
	public void clearMutation() {}

	@Override
	public String getMutationType() {
		return super.getMutationType();
	}
}