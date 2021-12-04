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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotassiumMutation extends EffectMutation implements IMutationTick {
	public PotassiumMutation(int _id) {
		super(_id,
				"potassium",
				"f74dfa9a-2104-403b-85a3-2a3f0c08e8c5",
				Rarity.UNCOMMON);
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
				player.world.createExplosion(
						null,
						player.getPosX(),
						player.getPosYHeight(0.0625D),
						player.getPosZ(),
						6f,
						Explosion.Mode.BREAK
				);
			}
		} else ((TickCounterInstance)getInstance(player)).reset();
	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| !legalPotions.contains(PotionUtils.getPotionFromItem(event.getItem()))
			) return;

		LivingEntity entity = event.getEntityLiving();

		entity.world.createExplosion(
				null,
				entity.getPosX(),
				entity.getPosYHeight(0.0625D),
				entity.getPosZ(),
				6f,
				Explosion.Mode.BREAK
		);
	}

	@Override
	public EffectMutationInstance putInstance(LivingEntity entity) {
		return new TickCounterInstance(entity, 60);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.HYDROPHILIC);
	}
}