package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class VisionMutation extends EffectMutation implements IMutationTick {
	public VisionMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.night_vision",
				WarpMutations.nameConst + "effect.blindness",
				"vision_icon.png",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19");
	}

	@Override
	public void attachListeners (IEventBus bus) {

	}

	@Override
	public void attachClientListeners(IEventBus bus) {
		bus.addListener(this::onRenderFog);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		instanceMap.forEach((uuid, mut) -> {
			if (!mut.isActive() || mut.getMutationLevel() == -1) return;

			if (mut.getParent().isPotionActive(Effects.NIGHT_VISION)) {
				for (EffectInstance e : mut.getParent().getActivePotionEffects()) {
					if (e.getDuration() < 1200) mut.getParent().addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 3600, 0, false, false));
				}
			}
			else {
				mut.getParent().addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 3600, 0, false, false));
			}
		});
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onRenderFog (EntityViewRenderEvent.FogDensity event) {
		if (!instanceMap.containsKey(Minecraft.getInstance().player.getUniqueID()) || instanceMap.get(Minecraft.getInstance().player.getUniqueID()).getMutationLevel() != -1) return;

		float density = event.getDensity();

		if (density < 0.1f) {
			density = 0.1f;
		}

		event.setCanceled(true);
		event.setDensity(density);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);
		EffectMutationInstance mut = instanceMap.get(entity.getUniqueID());

		if (mut.getMutationLevel() == 1) {
			entity.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1200, 0, false, false));
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		entity.removePotionEffect(Effects.NIGHT_VISION);
	}
}