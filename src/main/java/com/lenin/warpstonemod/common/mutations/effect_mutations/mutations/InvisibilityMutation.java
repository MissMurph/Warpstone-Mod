package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class InvisibilityMutation extends EffectMutation implements IMutationTick {
	public InvisibilityMutation(int _id) {
		super(_id,
				"invisibility",
				"invisibility.png",
				"a2361e8f-1be0-478f-9742-a873400e9b6d",
				Rarity.UNCOMMON);

	}

	@Override
	public void attachListeners(IEventBus bus){

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT || !instanceMap.containsKey(player.getUniqueID()) || !instanceMap.get(player.getUniqueID()).isActive()) return;

		if (!player.isInvisible()) player.setInvisible(true);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		entity.setInvisible(true);
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.setInvisible(false);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return manager.getCorruptionLevel() >= 2 && !manager.containsEffect(EffectMutations.GLOWING);
	}
}