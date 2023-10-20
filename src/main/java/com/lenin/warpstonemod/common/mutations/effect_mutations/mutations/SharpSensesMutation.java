package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mob_effects.WSEffects;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class SharpSensesMutation extends CounterMutation implements IMutationTick {
	public SharpSensesMutation(ResourceLocation _key) {
		super(_key,
                60
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
		bus.addListener(this::onPotionAdded);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
			) return;

		if (decrement(counterMap, player.getUUID()) && !player.hasEffect(WSEffects.SHARP_SENSES)) {
			ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

			if (attribute.getModifier(uuid) != null) attribute.removeModifier(uuid);
			attribute.addTransientModifier(new AttributeModifier(
					uuid,
					((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
					1.0f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));

			player.addEffect(new EffectInstance(WSEffects.SHARP_SENSES, 72000, 1, false, false, true));
		}
		else if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) == null) {
			player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(
					uuid,
					name + ".damage.boost",
					-0.25f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (event.getEntityLiving().level.isClientSide()
				|| !(event.getSource().getEntity() instanceof PlayerEntity)
				|| !containsInstance(event.getSource().getEntity().getUUID())
				|| !((PlayerEntity) event.getSource().getEntity()).hasEffect(WSEffects.SHARP_SENSES)
			) return;

		PlayerEntity player = (PlayerEntity) event.getSource().getEntity();

		if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
		player.removeEffect(WSEffects.SHARP_SENSES);
	}

	public void onPotionAdded (PotionEvent.PotionAddedEvent event) {
		if (!event.getEntityLiving().level.isClientSide
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| event.getPotionEffect().getEffect() != WSEffects.SHARP_SENSES
		) return;

		event.getEntityLiving().playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (manager.getParentEntity().level.isClientSide()) return;

		if (manager.getParentEntity().hasEffect(WSEffects.SHARP_SENSES)) manager.getParentEntity().removeEffect(WSEffects.SHARP_SENSES);
		if (manager.getParentEntity().getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) manager.getParentEntity().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
	}
}