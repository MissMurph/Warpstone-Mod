package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.WarpMutations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
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

		attachListeners(MinecraftForge.EVENT_BUS);
	}

	@Override
	public void attachListeners(IEventBus bus){
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

	@SubscribeEvent
	public void onEntityRender (RenderLivingEvent.Pre event) {
		instanceMap.forEach((uuid, mut) -> {
			if (!mut.isActive() || event.getEntity() != mut.getParent()) return;

			if (mut.getMutationLevel() == 1) event.getEntity().setInvisible(true);
			else event.getEntity().setGlowing(true);
		});
	}
}