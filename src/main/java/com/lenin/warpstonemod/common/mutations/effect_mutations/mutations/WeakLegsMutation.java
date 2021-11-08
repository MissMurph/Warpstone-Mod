package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WeakLegsMutation extends EffectMutation {
	public WeakLegsMutation(int _id) {
		super(_id,
				"weak_legs",
				"d198bf46-f9aa-4950-b0de-6f80d6396853",
				Rarity.COMMON);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onDamage(LivingDamageEvent event){
		LivingEntity entity = event.getEntityLiving();
		if (entity.world.isRemote() || !(entity instanceof PlayerEntity) || !(instanceMap.containsKey(entity.getUniqueID()))) return;

		if (event.getSource() == DamageSource.FALL) {
			float damage = (event.getAmount() * 1.25f);
			if (damage < 2) damage = 2;

			if (event.getSource() == DamageSource.FALL) event.setAmount(damage);
		}
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.JUMP_BOOST) && !manager.containsEffect(EffectMutations.STRONG_LEGS);
	}
}