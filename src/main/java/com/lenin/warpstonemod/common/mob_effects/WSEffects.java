package com.lenin.warpstonemod.common.mob_effects;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.potion.Effect;

import java.util.function.Supplier;

public class WSEffects {

	public static final Effect SHARP_SENSES = registerEffects("sharp_senses", SharpSensesEffect::new);
	public static final Effect TURTLE = registerEffects("turtle", TurtleEffect::new);
	public static final Effect WOOL = registerEffects("wool", WoolEffect::new);

	private static Effect registerEffects (String name, Supplier<Effect> effect) {
		Effect e = effect.get().setRegistryName(name);
		return Warpstone.getProxy().getRegistration().register(e);
	}

	public static void register() {}
}