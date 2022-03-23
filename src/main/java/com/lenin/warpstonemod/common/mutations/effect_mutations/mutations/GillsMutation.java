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

public class GillsMutation extends EffectMutation implements IMutationTick {
	public GillsMutation() {
		super(
                "gills",
				"bf69604d-0669-41d2-92e4-aafa8fa0acdc"
		);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !instanceMap.containsKey(player.getUniqueID())
				|| !instanceMap.get(player.getUniqueID()).isActive()) return;


		if (player.isPotionActive(Effects.WATER_BREATHING)) {
			for (EffectInstance e : player.getActivePotionEffects()) {
				if (e.getPotion() == Effects.WATER_BREATHING && e.getDuration() < 1200) {
					player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 3600, 0, false, false));
				}
			}
		}
		else {
			player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 3600, 0, false, false));
		}
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		manager.getParentEntity().addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 3600, 0, false, false));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		manager.getParentEntity().removePotionEffect(Effects.WATER_BREATHING);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.WEAK_LUNGS);
	}
}