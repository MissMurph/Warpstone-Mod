package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

public class GoodLuckMutation extends EffectMutation {
	public GoodLuckMutation() {
		super(
                "good_luck",
				"a2361e8f-1be0-478f-9742-a873400e9b6d",
				Rarity.COMMON);
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

		mut.getParent().getAttribute(Attributes.LUCK)
				.applyNonPersistentModifier(new AttributeModifier(uuid, "mutation.good_luck", 1.0D, AttributeModifier.Operation.ADDITION));
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
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BAD_LUCK);
	}
}