package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.MutationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class BlindnessMutation extends Mutation {
	public BlindnessMutation(ResourceLocation _key) {
		super(_key);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void attachClientListeners(IEventBus bus) {
		bus.addListener(this::onRenderFog);
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (!manager.getParentEntity().world.isRemote()) return;
		if (instanceMap.containsKey(manager.getUniqueId())) instanceMap.remove(manager.getUniqueId());
	}

	@OnlyIn(Dist.CLIENT)
	public void onRenderFog (EntityViewRenderEvent.FogDensity event) {
		if (!instanceMap.containsKey(Minecraft.getInstance().player.getUniqueID())
				|| !instanceMap.containsKey(Minecraft.getInstance().player.getUniqueID())
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
	public MutationInstance putClientInstance() {
		MutationInstance instance = new MutationInstance(MutateHelper.getClientManager());

		instanceMap.put(Minecraft.getInstance().player.getUniqueID(), instance);

		return instance;
	}
}