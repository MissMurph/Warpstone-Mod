package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.effect_mutations.mutations.*;

public class EffectMutations {
	public final static EffectMutation INVISIBILITY = WarpstoneMain.getEffectsMap().registerEffect(new InvisibilityMutation(0));
	public final static EffectMutation NIGHT_VISION = WarpstoneMain.getEffectsMap().registerEffect(new NightVisionMutation(1));
	public final static EffectMutation JUMP_BOOST = WarpstoneMain.getEffectsMap().registerEffect(new JumpBoostMutation(2));
	public final static EffectMutation GOOD_LUCK = WarpstoneMain.getEffectsMap().registerEffect(new GoodLuckMutation(3));
	public final static EffectMutation SLOW_FALLING = WarpstoneMain.getEffectsMap().registerEffect(new SlowFallMutation(4));
	public final static EffectMutation LEVITATION = WarpstoneMain.getEffectsMap().registerEffect(new LevitationMutation(5));
	public final static EffectMutation WEAK_LEGS = WarpstoneMain.getEffectsMap().registerEffect(new WeakLegsMutation(6));
	public final static EffectMutation BAD_LUCK = WarpstoneMain.getEffectsMap().registerEffect(new BadLuckMutation(7));
	public final static EffectMutation GLOWING = WarpstoneMain.getEffectsMap().registerEffect(new GlowingMutation(8));
	public final static EffectMutation BLINDNESS = WarpstoneMain.getEffectsMap().registerEffect(new SlowFallMutation(9));
	public final static EffectMutation FORTUNE = WarpstoneMain.getEffectsMap().registerEffect(new FortuneMutation(10));
	public final static EffectMutation FAST_METABOLISM = WarpstoneMain.getEffectsMap().registerEffect(new FastMetabolismMutation(11));
	public final static EffectMutation SLOW_METABOLISM = WarpstoneMain.getEffectsMap().registerEffect(new SlowMetabolismMutation(12));
	public final static EffectMutation CORROSIVE_TOUCH = WarpstoneMain.getEffectsMap().registerEffect(new CorrosiveTouchMutation(13));
	public final static EffectMutation FRAIL_BODY = WarpstoneMain.getEffectsMap().registerEffect(new FrailBodyMutation(14));

	public static void init() {}
}