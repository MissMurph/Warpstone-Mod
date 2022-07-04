package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.items.MutateItem;
import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class FastMetabolismMutation extends Mutation {
	public FastMetabolismMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !event.getItem().getItem().isFood()
				|| !instanceMap.containsKey(event.getEntityLiving().getUniqueID())
				|| event.getItem().getItem() instanceof MutateItem
		) return;

		PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
		playerEntity.getFoodStats().addStats(2, 4);
	}
}