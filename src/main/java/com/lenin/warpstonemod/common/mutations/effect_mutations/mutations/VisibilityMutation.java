package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class VisibilityMutation extends EffectMutation implements IMutationTick {
	public VisibilityMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.invisibility",
				WarpMutations.nameConst + "effect.glowing",
				"visibility_icon.png",
				"a2361e8f-1be0-478f-9742-a873400e9b6d");

	}

	@Override
	public void attachListeners(IEventBus bus){

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT ||
				!instanceMap.containsKey(player.getUniqueID()) ||
				!instanceMap.get(player.getUniqueID()).isActive()) return;

		if (instanceMap.get(player.getUniqueID()).getMutationLevel() == 1) {
			if (!player.isInvisible()) player.setInvisible(true);
		}

		if (instanceMap.get(player.getUniqueID()).getMutationLevel() == 1) {
			if (!player.isGlowing()) player.setInvisible(true);
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == 1) {
			entity.setInvisible(true);
		}

		if (instanceMap.get(entity.getUniqueID()).getMutationLevel() == -1) {
			entity.setGlowing(true);
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.setInvisible(false);
		entity.setGlowing(false);
	}
}