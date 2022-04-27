package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mob_effects.WSEffects;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TurtleMutation extends EffectMutation {
	public TurtleMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingAttack);
		bus.addListener(this::onLivingDamage);
	}

	public void onLivingAttack (LivingAttackEvent event) {
		//This event is fired BEFORE damage is applied so we can't change any modifiers to damage for the attacker

		if (event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
			) return;

		if (event.getEntityLiving().isActiveItemStackBlocking() && !event.getEntityLiving().isPotionActive(WSEffects.TURTLE)) {
			event.getEntityLiving().getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(new AttributeModifier(
					uuid,
					name + ".damage.boost",
					1.0f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));

			event.getEntityLiving().addPotionEffect(new EffectInstance(WSEffects.TURTLE, 72000, 0, false, false, true));
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		//This event is fired AFTER damage is applied, so we remove the damage modifier here

		if (event.getEntityLiving().world.isRemote()
				|| !(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
		) return;

		PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

		if (player.isPotionActive(WSEffects.TURTLE)) {
			player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
			player.removePotionEffect(WSEffects.TURTLE);
		}
	}
}