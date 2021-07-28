package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.VisibilityMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.PpMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.VisionMutation;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;

import java.util.Map;
import java.util.function.Supplier;

public class MutationsRegistry {
	public static Map<Integer, EffectMutation.IEffectFactory> factoryMap = new Object2ObjectArrayMap<>();

	public static void init(){
		registerFactory(VisibilityMutation.EffectFactory::new);
		registerFactory(PpMutation.EffectFactory::new);
		registerFactory(VisionMutation.EffectFactory::new);

		for (int i = 0; i < factoryMap.size(); i++) {
			System.out.println("Factory IDS: " + factoryMap.get(i).getID());
		}
	}

	public static EffectMutation constructMutation (int key, LivingEntity parent, int mutationLevel){
		return factoryMap.get(key).factory(parent, mutationLevel);
	}

	public static <T extends EffectMutation.IEffectFactory> void registerFactory (Supplier<T> supplier){
		EffectMutation.IEffectFactory factory = supplier.get();
		int size = factoryMap.size();;
		factory.setID(size);
		factoryMap.put(size, factory);
	}

	public static int getMapSize () {
		return factoryMap.size();
	}
}