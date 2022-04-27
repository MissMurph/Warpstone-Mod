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

		if (decrement(counterMap, player.getUniqueID())) {
			if (player.areEyesInFluid(FluidTags.WATER)
					&& !player.world.getBlockState(new BlockPos(player.getPosX(), player.getPosYEye(), player.getPosZ())).matchesBlock(Blocks.BUBBLE_COLUMN)
					&& !EffectUtils.canBreatheUnderwater(player)
					&& !player.abilities.disableDamage
			) {
				int air = player.getAir() - 1;
				player.setAir(air);
			} else {
				reset(counterMap, player.getUniqueID());
			}
		}
	}
}