package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class FastMetabolismMutation extends EffectMutation {
	public FastMetabolismMutation() {
		super(
                "fast_metabolism",
				"1ce59983-cba5-4586-9186-3f69bd0487ce"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !event.getItem().getItem().isFood()
				|| !instanceMap.containsKey(event.getEntityLiving().getUniqueID())
				|| !instanceMap.get(event.getEntityLiving().getUniqueID()).isActive()
				|| event.getItem().getItem() instanceof IWarpstoneConsumable
		) return;

		PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
		playerEntity.getFoodStats().addStats(2, 4);
	}
}