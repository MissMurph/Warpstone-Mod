package com.lenin.warpstonemod.common.items.warpstone_consumables;

public interface IWarpstoneConsumable {
	int getCorruption();
	int getInstability();
	int getWither();

	boolean canBeConsumed ();
}