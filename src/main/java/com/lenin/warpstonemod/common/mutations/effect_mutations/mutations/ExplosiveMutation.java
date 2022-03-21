package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExplosiveMutation extends EffectMutation {
	public ExplosiveMutation() {
		super(
                "explosive",
				"7332e11c-ff66-439f-8808-4de93e9cf355"
		);
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
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		event.getEntityLiving().attackEntityFrom(DamageSource.ON_FIRE, 1f);
	}

	public void onLivingDeath (LivingDeathEvent event) {
		if (!fireSources.contains(event.getSource())
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		LivingEntity entity = event.getEntityLiving();

		entity.world.createExplosion(entity, entity.getPosX(), entity.getPosYHeight(0.0625D), entity.getPosZ(), 4.0F, Explosion.Mode.BREAK);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.FIRE_BREATHING);
	}
}