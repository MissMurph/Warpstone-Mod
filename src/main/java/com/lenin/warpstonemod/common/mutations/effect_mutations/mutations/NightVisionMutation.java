package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class NightVisionMutation extends EffectMutation implements IMutationTick {
	public NightVisionMutation() {
		super(
                "night_vision",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19",
				MutationTags.COMMON
		);
	}

	@Override
	public void attachListeners (IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {
	}

	@Override
	public void clearInstance(LivingEntity entity) {
		super.clearInstance(entity);

		if (!entity.world.isRemote()) return;
		if (instanceMap.containsKey(entity.getUniqueID())) instanceMap.remove(entity.getUniqueID());
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !instanceMap.containsKey(player.getUniqueID())
				|| !instanceMap.get(player.getUniqueID()).isActive()) return;


		if (player.isPotionActive(Effects.NIGHT_VISION)) {
			for (EffectInstance e : player.getActivePotionEffects()) {
				if (e.getPotion() == Effects.NIGHT_VISION && e.getDuration() < 1200) {
					player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 3600, 0, false, false));
				}
			}
		}
		else {
			player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 3600, 0, false, false));
		}
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		entity.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1200, 0, false, false));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.removePotionEffect(Effects.NIGHT_VISION);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BLINDNESS);
	}
}