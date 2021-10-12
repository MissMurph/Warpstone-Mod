package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;

import java.util.Map;

public class EffectsMap {
	public static Map<Integer, EffectMutation> effectMap = new Object2ObjectArrayMap<>();

	public static void init(){
		//registerEffect(new VisibilityMutation.EffectFactory());
		//registerEffect(PpMutation.EffectFactory::new);
		//registerEffect(VisionMutation.EffectFactory::new);
	}

	public static EffectMutation constructInstance (int key, LivingEntity parent, int mutationLevel){
		EffectMutation mut = effectMap.get(key);
		mut.putInstance(parent, mutationLevel);
		return mut;
	}

	public static EffectMutation registerEffect (EffectMutation mut){
		int key = effectMap.size();
		effectMap.put(key, mut);
		return mut;
	}

	public static int getMapSize () {
		return effectMap.size();
	}
}