package com.lenin.warpstonemod.common.items.warpstone_consumables;

import net.minecraft.nbt.CompoundNBT;

public interface IWarpstoneConsumable {
	int getCorruption();
	int getInstability();
	int getWither();

	CompoundNBT serialize();
	void deserialize();

	boolean canBeConsumed ();
}