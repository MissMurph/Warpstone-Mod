package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class WeakLiverMutation extends EffectMutation {
	public WeakLiverMutation() {
		super(
                "weak_liver",
				"76635d17-d433-4464-bf8e-dc1a60d55229"
		);
	}

	/**This mutation inflicts Nausea on the player whenever they drink a potion
	 *
	 *
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	public void onItemUseFinish (LivingEntityUseItemEvent.Finish event){
		if (event.getEntityLiving().world.isRemote()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| PotionUtils.getEffectsFromStack(event.getItem()).isEmpty()
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
		) return;

		int ticks = (int) Math.max(200, Math.random() * 400);
		event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.NAUSEA, ticks));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		entity.removePotionEffect(Effects.NAUSEA);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.ALCOHOLIC);
	}
}