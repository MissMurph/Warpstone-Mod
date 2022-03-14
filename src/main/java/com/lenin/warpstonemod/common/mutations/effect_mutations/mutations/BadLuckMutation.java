package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.IEventBus;

public class BadLuckMutation extends EffectMutation {
	public BadLuckMutation() {
		super(
                "bad_luck",
				"0942e8e9-295a-430f-9988-5537e4010648",
				MutationTags.COMMON,
				MutationTags.NEGATIVE
		);
	}

	@Override
	public void attachListeners(IEventBus bus) { }

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote) return;

		EffectMutationInstance mut = instanceMap.get(entity.getUniqueID());

		mut.getParent()
				.getAttribute(Attributes.LUCK)
				.applyNonPersistentModifier(new AttributeModifier(uuid, "mutation.bad_luck", -1.0D, AttributeModifier.Operation.ADDITION));
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote) return;

		instanceMap.get(entity.getUniqueID()).getParent()
				.getAttribute(Attributes.LUCK)
				.removeModifier(uuid);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.GOOD_LUCK);
	}
}