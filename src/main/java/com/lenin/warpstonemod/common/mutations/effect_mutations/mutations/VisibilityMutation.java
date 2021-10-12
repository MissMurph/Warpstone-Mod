package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VisibilityMutation extends EffectMutation {
	public VisibilityMutation(int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.invisibility",
				WarpMutations.nameConst + "effect.glowing",
				"visibility_icon.png",
				"a2361e8f-1be0-478f-9742-a873400e9b6d");

	}

	@Override
	public void attachListeners(IEventBus bus){


	}

	//@OnlyIn(Dist.CLIENT)
	@Override
	public void attachClientListeners(IEventBus bus) {
		bus.addListener(this::onEntityRender);
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		switch (instanceMap.get(entity.getUniqueID()).getMutationLevel()) {
			case -1:
				entity.setGlowing(true);
				break;
			case 0:
				break;
			case 1:
				entity.setInvisible(true);
				break;
		}
	}

	@Override
	public void clearMutation(LivingEntity entity) {
		super.clearMutation(entity);

		entity.setInvisible(false);
		entity.setGlowing(false);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onEntityRender (RenderLivingEvent.Pre event) {
		instanceMap.forEach((uuid, mut) -> {
			if (!mut.isActive() || event.getEntity() != mut.getParent()) return;

			if (mut.getMutationLevel() == 1) event.getEntity().setInvisible(true);
			else event.getEntity().setGlowing(true);
		});
	}
}