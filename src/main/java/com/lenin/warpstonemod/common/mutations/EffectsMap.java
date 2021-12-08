package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;

import java.util.Map;

public class EffectsMap {
	public Map<Integer, EffectMutation> effectMap = new Object2ObjectArrayMap<>();

	public void init(){
		EffectMutations.init();
	}

	public EffectMutation constructInstance (int key, LivingEntity parent){
		EffectMutation mut = effectMap.get(key);
		mut.putInstance(parent);
		return mut;
	}

	public EffectMutation registerEffect (EffectMutation mut){
		if (effectMap.containsKey(mut.getMutationID())) throw new IllegalArgumentException("ID Already Registered");
		int key = effectMap.size();
		effectMap.put(key, mut);
		return mut;
	}

	public int getMapSize () {
		return effectMap.size();
	}

	public Map<Integer, EffectMutation> getMap () {
		return effectMap;
	}

	public EffectMutation getEffectMutation (EffectMutation mut) {
		return getEffectMutation(mut.getMutationID());
	}

	public EffectMutation getEffectMutation (int id) {
		return effectMap.get(id);
	}

	public EffectMutation getEffectMutation (String name) {
		for (EffectMutation e : effectMap.values()) {
			if (e.getMutationName().toString().equals(name)) return e;
		}
		return null;
	}
}