package com.lenin.warpstonemod;

import net.minecraft.item.Food;

public class WarpFoods {
	public static final Food WARPSTONE_DUST = (new Food.Builder()).hunger(0).saturation(0).setAlwaysEdible().fastToEat().build();

	public static void register () {}
}