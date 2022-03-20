package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mob_effects.WarpEffects;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TurtleMutation extends EffectMutation {
	public TurtleMutation() {
		super(
                "turtle",
				"21fdd2d1-a7d3-44fd-a033-d155775e5d95"
		);

		this.textureResource = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/mob_effect/turtle.png");
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingAttack);
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingAttack (LivingAttackEvent event) {
		//This event is fired BEFORE damage is applied so we can't change any modifiers to damage for the attacker

		if (event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
			) return;

		if (event.getEntityLiving().isActiveItemStackBlocking() && !event.getEntityLiving().isPotionActive(WarpEffects.TURTLE)) {
			event.getEntityLiving().getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(new AttributeModifier(
					uuid,
					((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
					1.0f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));

			event.getEntityLiving().addPotionEffect(new EffectInstance(WarpEffects.TURTLE, 72000, 0, false, false, true));
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		//This event is fired AFTER damage is applied, so we remove the damage modifier here

		if (event.getEntityLiving().world.isRemote()
				|| !(event.getSource().getTrueSource() instanceof PlayerEntity)
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
				|| !getInstance(event.getSource().getTrueSource().getUniqueID()).isActive()
		) return;

		PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

		if (player.isPotionActive(WarpEffects.TURTLE)) {
			player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
			player.removePotionEffect(WarpEffects.TURTLE);
		}
	}
}