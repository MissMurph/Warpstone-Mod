package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterEffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class WeakLungsMutation extends CounterEffectMutation implements IMutationTick {
	public WeakLungsMutation() {
		super(
				"weak_lungs",
				"9216454f-c64d-4dcd-95f3-339df891aeef",
				5
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	/**This code is replicating the behaviours of {@link LivingEntity#baseTick()} <br>
	 * We perform the same check for if the player is under water then every 5 ticks
	 * take away 1 unit of breath, making effectively +20% air consumption
	 */

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
				|| !getInstance(player).isActive()
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

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.GILLS);
	}
}