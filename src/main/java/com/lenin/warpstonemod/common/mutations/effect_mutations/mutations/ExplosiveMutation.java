package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExplosiveMutation extends Mutation {
	public ExplosiveMutation(ResourceLocation _key) {
		super(_key);
	}

	private final Set<DamageSource> fireSources = new HashSet<>(Arrays.asList(
			DamageSource.ON_FIRE,
			DamageSource.IN_FIRE,
			DamageSource.HOT_FLOOR,
			DamageSource.LAVA
	));

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDeath);
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onLivingDamage (LivingDeathEvent event) {
		if (!fireSources.contains(event.getSource())
				|| !containsInstance(event.getEntityLiving())
		) return;

		event.getEntityLiving().attackEntityFrom(DamageSource.ON_FIRE, 1f);
	}

	public void onLivingDeath (LivingDeathEvent event) {
		if (!fireSources.contains(event.getSource())
				|| !containsInstance(event.getEntityLiving())
		) return;

		LivingEntity entity = event.getEntityLiving();

		entity.world.createExplosion(entity, entity.getPosX(), entity.getPosYHeight(0.0625D), entity.getPosZ(), 4.0F, Explosion.Mode.BREAK);
	}
}