package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class NightVisionMutation extends EffectMutation implements IMutationTick {
	public NightVisionMutation() {
		super(
                "night_vision",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19"
		);
	}

	@Override
	public void attachListeners (IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {
	}

	@Override
	public void clearInstance(PlayerManager manager) {
		super.clearInstance(manager);

		if (!manager.world.isRemote()) return;
		if (instanceMap.containsKey(manager.getUniqueID())) instanceMap.remove(manager.getUniqueID());
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
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.world.isRemote()) return;

		manager.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1200, 0, false, false));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.world.isRemote()) return;

		manager.removePotionEffect(Effects.NIGHT_VISION);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BLINDNESS);
	}
}