package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

public class EffectMutationInstance {
	private EffectMutation effect;
	private LivingEntity parent;
	private int mutationLevel;

	private boolean active;

	public EffectMutationInstance (EffectMutation _effect, int _mutationLevel, LivingEntity _parent){
		effect = _effect;
		mutationLevel = _mutationLevel;
		parent = _parent;

		active = false;
	}

	public int getMutationLevel () {
		return mutationLevel;
	}

	public LivingEntity getParent (){
		return parent;
	}

	public boolean isActive () {
		return active;
	}

	public void setActive (boolean value){
		active = value;
	}
}