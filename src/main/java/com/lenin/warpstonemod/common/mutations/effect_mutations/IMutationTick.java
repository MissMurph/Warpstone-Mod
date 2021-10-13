package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.player.PlayerEntity;

public interface IMutationTick {
	void mutationTick (PlayerEntity player);
}