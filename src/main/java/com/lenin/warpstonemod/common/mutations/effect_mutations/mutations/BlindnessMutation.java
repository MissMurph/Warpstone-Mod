package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class BlindnessMutation extends EffectMutation {
	public BlindnessMutation(int _id) {
		super(_id,
				"blindness",
				"0d988324-bfef-4dd4-87a7-647364829c44",
				Rarity.COMMON);
	}

	@Override
	public void attachListeners (IEventBus bus) {

	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void attachClientListeners(IEventBus bus) {
		bus.addListener(this::onRenderFog);
	}

	@Override
	public void clearInstance(LivingEntity entity) {
		super.clearInstance(entity);

		if (!entity.world.isRemote()) return;
		if (instanceMap.containsKey(entity.getUniqueID())) instanceMap.remove(entity.getUniqueID());
	}

	@OnlyIn(Dist.CLIENT)
	public void onRenderFog (EntityViewRenderEvent.FogDensity event) {
		if (!instanceMap.containsKey(Minecraft.getInstance().player.getUniqueID())
				|| !instanceMap.containsKey(Minecraft.getInstance().player.getUniqueID())
				|| !instanceMap.get(Minecraft.getInstance().player.getUniqueID()).isActive()
		) return;

		float density = event.getDensity();

		if (density < 0.1f) {
			density = 0.1f;
		}

		event.setCanceled(true);
		event.setDensity(density);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public EffectMutationInstance putClientInstance() {
		EffectMutationInstance instance = new EffectMutationInstance(Minecraft.getInstance().player);

		instanceMap.put(Minecraft.getInstance().player.getUniqueID(), instance);

		return instance;
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) {
			instanceMap.get(Minecraft.getInstance().player.getUniqueID()).setActive(true);
		}
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) {
			instanceMap.get(Minecraft.getInstance().player.getUniqueID()).setActive(false);
		}
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.NIGHT_VISION);
	}
}