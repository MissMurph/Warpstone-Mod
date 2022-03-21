package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ThornsMutation extends EffectMutation {
	public ThornsMutation() {
		super(
                "thorns",
				"cd74624b-9d68-4017-b4ab-eb326f45dd72"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| event.getSource().getTrueSource() == null
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		float damage = event.getAmount() * 0.25f;
		event.getSource().getTrueSource().attackEntityFrom(DamageSource.causeThornsDamage(event.getEntityLiving()), damage);
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.world.isRemote) return;

		manager.getAttribute(Attributes.ARMOR).applyNonPersistentModifier(new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.world.isRemote) return;

		manager.getAttribute(Attributes.ARMOR).removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.SCALES);
	}
}