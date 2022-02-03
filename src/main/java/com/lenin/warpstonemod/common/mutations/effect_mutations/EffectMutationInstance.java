package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

public class EffectMutationInstance {
	protected final LivingEntity parent;

	protected boolean active;

	public EffectMutationInstance(LivingEntity _parent){
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