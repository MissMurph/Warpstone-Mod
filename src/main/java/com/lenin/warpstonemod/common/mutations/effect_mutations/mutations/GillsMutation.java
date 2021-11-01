package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class GillsMutation extends EffectMutation implements IMutationTick {
	public GillsMutation(int _id) {
		super(_id,
				"gills",
				"bf69604d-0669-41d2-92e4-aafa8fa0acdc",
				Rarity.COMMON);
	}

	@Override
	public void attachListeners(IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {

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
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote) return;

		entity.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 3600, 0, false, false));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote) return;

		entity.removePotionEffect(Effects.WATER_BREATHING);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.WEAK_LUNGS);
	}
}