package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class EffectMutationInstance {
	protected final LivingEntity parent;

	protected boolean active;

	//protected Map<String, Integer> map = new HashMap<>();

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

	//Below is per player universal data storage, should multiple mutations need to track explicit values then we uncomment and use it
	/*public void storeValue (String key, int i) {
		map.put(key, i);
	}

	public int readValue (String key){
		if (!map.containsKey(key)) {
			throw new IllegalArgumentException("No key found");
		}

		return map.get(key);
	}*/
}