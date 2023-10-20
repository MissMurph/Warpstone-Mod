package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotassiumMutation extends CounterMutation implements IMutationTick {
	public PotassiumMutation(ResourceLocation _key) {
		super(_key,
                100
		);
	}

	private final List<Potion> legalPotions = new ArrayList<>(Arrays.asList(
			Potions.WATER,
			Potions.MUNDANE,
			Potions.AWKWARD,
			Potions.THICK
	));

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
			) return;

		if (player.isInWaterOrRain()) {
			if (decrement(counterMap, player.getUUID())) {
				player.level.explode(
						player,
						player.getX(),
						player.getY(0.0625D),
						player.getZ(),
						6f,
						Explosion.Mode.BREAK
				);
			}
		} else reset(counterMap, player.getUUID());
	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving().level.isClientSide()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| !legalPotions.contains(PotionUtils.getPotion(event.getItem()))
			) return;

		LivingEntity entity = event.getEntityLiving();

		entity.level.explode(
				null,
				entity.getX(),
				entity.getY(0.0625D),
				entity.getZ(),
				6f,
				Explosion.Mode.BREAK
		);
	}
}