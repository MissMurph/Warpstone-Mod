package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.IEventBus;

public class BadLuckMutation extends EffectMutation {
	public BadLuckMutation() {
		super(
                "bad_luck",
				"0942e8e9-295a-430f-9988-5537e4010648"
		);
	}

	@Override
	public void attachListeners(IEventBus bus) { }

	@Override
	public void attachClientListeners(IEventBus bus) {

	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		EffectMutationInstance mut = instanceMap.get(manager.getUniqueId());

		mut.getParent()
				.getAttribute(Attributes.LUCK.getAttributeName())
				.applyNonPersistentModifier(new AttributeModifier(uuid, "mutation.bad_luck", -1.0D, AttributeModifier.Operation.ADDITION));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.getParentEntity().world.isRemote) return;

		instanceMap.get(manager.getUniqueID()).getParent()
				.getAttribute(Attributes.LUCK)
				.removeModifier(uuid);
	}

	@Override
	public IFormattableTextComponent getMutationName() {
		return super.getMutationName().mergeStyle(TextFormatting.RED);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.GOOD_LUCK);
	}
}