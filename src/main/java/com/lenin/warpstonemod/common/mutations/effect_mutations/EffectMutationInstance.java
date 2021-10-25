package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

public class EffectMutationInstance {
	private EffectMutation effect;
	private LivingEntity parent;

	private boolean active;

	public EffectMutationInstance (EffectMutation _effect, LivingEntity _parent){
		effect = _effect;
		parent = _parent;

		active = false;
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