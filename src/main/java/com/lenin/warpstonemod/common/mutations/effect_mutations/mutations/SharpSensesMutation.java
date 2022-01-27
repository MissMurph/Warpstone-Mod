package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mob_effects.WarpEffects;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class SharpSensesMutation extends CounterEffectMutation implements IMutationTick {
	public SharpSensesMutation(int _id) {
		super(_id,
				"sharp_senses",
				"6ba9291c-f067-410b-9579-9f11169ea0fd",
				Rarity.RARE,
				60
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
				|| !getInstance(player).isActive()
			) return;

		if (deincrement(counterMap, player.getUniqueID()) && !player.isPotionActive(WarpEffects.SHARP_SENSES.get())) {
			ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

			if (attribute.getModifier(uuid) != null) attribute.removeModifier(uuid);
			attribute.applyNonPersistentModifier(new AttributeModifier(
					uuid,
					((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
					1.0f,
					AttributeModifier.Operation.MULTIPLY_TOTAL
			));

			player.addPotionEffect(new EffectInstance(WarpEffects.SHARP_SENSES.get(), 72000, 1, false, false, true));
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
				|| !((PlayerEntity) event.getSource().getTrueSource()).isPotionActive(WarpEffects.SHARP_SENSES.get())
			) return;

		PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

		if (player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
		player.removePotionEffect(WarpEffects.SHARP_SENSES.get());
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		if (entity.isPotionActive(WarpEffects.SHARP_SENSES.get())) entity.removePotionEffect(WarpEffects.SHARP_SENSES.get());
		if (entity.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(uuid) != null) entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.CLAWS);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(WarpstoneMain.MOD_ID, "textures/mob_effect/sharp_senses.png");
	}
}