package com.lenin.warpstonemod.common;

import net.minecraft.item.Food;

public class WSFoods {
	public static final Food MUTATE_ITEM = (new Food.Builder()).hunger(0).saturation(0).setAlwaysEdible().fastToEat().build();

	public static void register () {}
}