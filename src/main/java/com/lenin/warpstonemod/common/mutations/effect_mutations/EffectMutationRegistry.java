package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.EffectsMap;

public class EffectMutationRegistry {
	public static final EffectMutation VISIBILITY_EFFECT = EffectsMap.registerEffect(new VisibilityMutation(0));
	public static final EffectMutation VISION_EFFECT = EffectsMap.registerEffect(new VisionMutation(1));

	public static void init() {}
}