package com.lenin.warpstonemod.common;

import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class WSFoods {
	public static final Food MUTATE_ITEM = (new Food.Builder())
			.hunger(0)
			.saturation(0)
			.setAlwaysEdible()
			.fastToEat()
			.build();

	public static final Food HUMANT_MEAT = (new Food.Builder())
			.hunger(6)
			.saturation(0.1F)
			.effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.8F)
			.meat()
			.build();

	public static void register () {}
}