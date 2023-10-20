package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;

public class WeakLungsMutation extends CounterMutation implements IMutationTick {
	public WeakLungsMutation(ResourceLocation _key) {
		super(_key,
				5
		);
	}

	/**This code is replicating the behaviours of {@link LivingEntity#baseTick()} <br>
	 * We perform the same check for if the player is under water then every 5 ticks
	 * take away 1 unit of breath, making effectively +20% air consumption
	 */

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
		) return;

		if (decrement(counterMap, player.getUUID())) {
			if (player.isEyeInFluid(FluidTags.WATER)
					&& !player.level.getBlockState(new BlockPos(player.getX(), player.getEyeY(), player.getZ())).is(Blocks.BUBBLE_COLUMN)
					&& !EffectUtils.hasWaterBreathing(player)
					&& !player.abilities.invulnerable
			) {
				int air = player.getAirSupply() - 1;
				player.setAirSupply(air);
			} else {
				reset(counterMap, player.getUUID());
			}
		}
	}
}