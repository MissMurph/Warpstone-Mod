package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;

import java.util.Map;

public class EffectsMap {
	public Map<Integer, EffectMutation> effectMap = new Object2ObjectArrayMap<>();

	public void init(){
		registerEffect(new VisibilityMutation(0));
		registerEffect(new VisionMutation(1));
		registerEffect(new JumpMutation(2));
		registerEffect(new LuckMutation(3));
		registerEffect(new FallingMutation(4));
	}

	public EffectMutation constructInstance (int key, LivingEntity parent, int mutationLevel){
		EffectMutation mut = effectMap.get(key);
		if (!parent.world.isRemote) mut.putInstance(parent, mutationLevel);
		return mut;
	}

	public EffectMutation registerEffect (EffectMutation mut){
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
}