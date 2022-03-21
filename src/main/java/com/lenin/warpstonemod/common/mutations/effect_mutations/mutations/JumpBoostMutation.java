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

public class JumpBoostMutation extends EffectMutation implements IMutationTick {
	public JumpBoostMutation() {
		super(
                "jump_boost",
				"1020d46e-68db-45f4-9721-b14608ade167"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {
	}

	@Override
	public void attachClientListeners(IEventBus bus) {
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (!instanceMap.containsKey(player.getUniqueID())
				|| !instanceMap.get(player.getUniqueID()).isActive()
		) return;

		if (player.isPotionActive(Effects.JUMP_BOOST)) {
			for (EffectInstance e : player.getActivePotionEffects()) {
				if (e.getPotion() == Effects.JUMP_BOOST && e.getDuration() < 1200) {
					player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
				}
			}
		}
		else {
			player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
		}
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.world.isRemote()) return;

		manager.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 0, false, false));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.world.isRemote()) return;

		manager.removePotionEffect(Effects.JUMP_BOOST);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.WEAK_LEGS);
	}
}