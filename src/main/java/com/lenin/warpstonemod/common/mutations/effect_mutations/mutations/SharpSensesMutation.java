package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mob_effects.WarpEffects;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterEffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
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

public class SharpSensesMutation extends CounterEffectMutation implements IMutationTick {
	public SharpSensesMutation() {
		super(
                "sharp_senses",
				"6ba9291c-f067-410b-9579-9f11169ea0fd",
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
				|| !getInstance(player).isActive()
			) return;

		if (decrement(counterMap, player.getUniqueID()) && !player.isPotionActive(WarpEffects.SHARP_SENSES)) {
			ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

			if (attribute.getModifier(uuid) != null) attribute.removeModifier(uuid);
			attribute.applyNonPersistentModifier(new AttributeModifier(
					uuid,
					((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
					1.0f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));

			player.addPotionEffect(new EffectInstance(WarpEffects.SHARP_SENSES, 72000, 1, false, false, true));
		}
		else if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) == null) {
			player.getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(new AttributeModifier(
					uuid,
					((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
					-0.25f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (event.getEntityLiving().world.isRemote
				|| !(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
				|| !getInstance(event.getSource().getTrueSource().getUniqueID()).isActive()
				|| !((PlayerEntity) event.getSource().getTrueSource()).isPotionActive(WarpEffects.SHARP_SENSES)
			) return;

		PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

		if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
		player.removePotionEffect(WarpEffects.SHARP_SENSES);
	}

	public void onPotionAdded (PotionEvent.PotionAddedEvent event) {
		if (!event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| event.getPotionEffect().getPotion() != WarpEffects.SHARP_SENSES
		) return;

		event.getEntityLiving().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		if (manager.getParentEntity().isPotionActive(WarpEffects.SHARP_SENSES)) manager.getParentEntity().removePotionEffect(WarpEffects.SHARP_SENSES);
		if (manager.getParentEntity().getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) manager.getParentEntity().getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
	}
}