package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.LogicalSide;

public interface IMutationTick {
	void mutationTick(PlayerEntity player, LogicalSide side);
}