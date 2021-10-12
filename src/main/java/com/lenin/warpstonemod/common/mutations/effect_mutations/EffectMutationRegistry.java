package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.EffectsMap;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;

public class EffectMutationRegistry {
	public static final EffectMutation VISIBILITY_EFFECT = EffectsMap.registerEffect(new VisibilityMutation(0));
	public static final EffectMutation VISION_EFFECT = EffectsMap.registerEffect(new VisionMutation(1));
	public static final EffectMutation JUMP_EFFECT = EffectsMap.registerEffect(new JumpMutation(2));
	public static final EffectMutation LUCK_EFFECT = EffectsMap.registerEffect(new LuckMutation(3));
	public static final EffectMutation FALLING_EFFECT = EffectsMap.registerEffect(new FallingMutation(4));

	public static void init() {}
}