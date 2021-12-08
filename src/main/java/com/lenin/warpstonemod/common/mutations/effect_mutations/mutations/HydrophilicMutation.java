package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HydrophilicMutation extends EffectMutation implements IMutationTick {
	public HydrophilicMutation(int _id) {
		super(_id,
				"hydriophilic",
				"17782c2e-2438-4c81-b05b-507cb3c576b0",
				Rarity.UNCOMMON);
	}

	/**This mutation replaces eating food with drinking water, you cannot eat food
	 * and must drink water in order to eat, blank potions yield less food. <br>
	 * <br>
	 * Standing in water or rain will slowly regain 1 food every 5 seconds <br>
	 * <br>
	 * {@link Potions#WATER} = 4 <br>
	 * {@link Potions#MUNDANE} = 3 <br>
	 * {@link Potions#AWKWARD} = 2 <br>
	 * {@link Potions#THICK} = 1 <br>
	 */

	private final List<Potion> legalPotions = new ArrayList<>(Arrays.asList(
			Potions.WATER,
			Potions.MUNDANE,
			Potions.AWKWARD,
			Potions.THICK
	));

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseStart);
		bus.addListener(this::onItemUseFinish);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
				|| !getInstance(player).isActive()
			) return;

		if (player.isInWaterRainOrBubbleColumn()) {
			if (((TickCounterInstance)getInstance(player)).deincrement()) {
				player.getFoodStats().addStats(1, 0);
			}
		}
		else ((TickCounterInstance)getInstance(player)).reset();
	}

	public void onItemUseStart (PlayerInteractEvent.RightClickItem event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
			) return;

		if (event.getItemStack().isFood()) {
			event.setCanceled(true);
			return;
		}

		if (legalPotions.contains(PotionUtils.getPotionFromItem(event.getItemStack())) && ((PlayerEntity) event.getEntityLiving()).getFoodStats().getFoodLevel() == 20) {
			event.setCanceled(true);
		}
	}

	/** We alter the amount of food gained from each potion type that's just water with a different name
	 * This is done by reducing the food value from 4 by the index of the {@link Potion} type in {@link #legalPotions}
	 */

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving().world.isRemote
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| !legalPotions.contains(PotionUtils.getPotionFromItem(event.getItem()))
		) return;

		int foodValue = 4 - legalPotions.indexOf(PotionUtils.getPotionFromItem(event.getItem()));

		((PlayerEntity) event.getEntityLiving()).getFoodStats().addStats(foodValue, 1);
	}

	@Override
	public EffectMutationInstance getInstanceType(LivingEntity entity) {
		return new TickCounterInstance(entity, 100);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.POTASSIUM);
	}
}