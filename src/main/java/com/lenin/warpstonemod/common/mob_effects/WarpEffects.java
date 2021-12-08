package com.lenin.warpstonemod.common.mob_effects;

import com.lenin.warpstonemod.common.Registration;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;

public class WarpEffects {

	public static final RegistryObject<Effect> SHARP_SENSES = Registration.EFFECTS.register("sharp_senses", SharpSensesEffect::new);
	public static final RegistryObject<Effect> TURTLE = Registration.EFFECTS.register("turtle", TurtleEffect::new);

	public static void init() {}
}