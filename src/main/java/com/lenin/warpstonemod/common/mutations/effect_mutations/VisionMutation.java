package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.MutationsRegistry;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class VisionMutation extends EffectMutation{
	private final EffectInstance effect = new EffectInstance(Effects.NIGHT_VISION, 20, 0, false, false);
	private boolean isActive;

	protected VisionMutation(LivingEntity _parentPlayer, int _mutationLevel) {
		super(_parentPlayer, EffectFactory.id, _mutationLevel,
				WarpMutations.nameConst + "effect.night_vision",
				WarpMutations.nameConst + "effect.blindness",
				"vision_icon.png",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19");


		attachListeners(MinecraftForge.EVENT_BUS);
	}

	protected void attachListeners (IEventBus bus) {
		bus.addListener(this::onRenderFog);
		bus.addListener(this::onPotionExpiry);
		bus.addListener(this::onPotionRemoved);
	}

	public void onRenderFog (EntityViewRenderEvent.FogDensity event) {
		if (mutationLevel != -1 && !isActive) return;

		float density = event.getDensity();

		if (density < 0.1f) {
			density = 0.1f;
			event.setDensity(density);
			event.setCanceled(true);
		}
	}

	public void onPotionRemoved (PotionEvent.PotionRemoveEvent event) {
		if (mutationLevel != 1) return;

		if (event.getEntity() == parentPlayer && event.getPotionEffect().getPotion() == Effects.NIGHT_VISION){
			parentPlayer.addPotionEffect(effect);
		}
	}

	public void onPotionExpiry (PotionEvent.PotionExpiryEvent event){
		if (mutationLevel != 1) return;

		if (event.getEntity() == parentPlayer && event.getPotionEffect().getPotion() == Effects.NIGHT_VISION){
			parentPlayer.addPotionEffect(effect);
		}
	}

	@Override
	public void applyMutation() {
		super.applyMutation();

		switch (mutationLevel) {
			case -1:
				isActive = true;
				break;
			case 0:
				break;
			case 1:
				parentPlayer.addPotionEffect(effect);
		}
	}

	@Override
	public void clearMutation() {
		super.clearMutation();

		isActive = false;
	}

	public static class EffectFactory implements IEffectFactory {
		public EffectFactory() { }

		protected static int id;

		@Override
		public int getID() {
			return id;
		}

		@Override
		public void setID(int value){
			id = value;
		}

		@Override
		public EffectMutation factory(LivingEntity parent, int _mutationLevel) {
			return new VisionMutation(parent, _mutationLevel);
		}
	}
}