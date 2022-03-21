package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;

public class GoodLuckMutation extends EffectMutation {
	public GoodLuckMutation() {
		super(
                "good_luck",
				"a2361e8f-1be0-478f-9742-a873400e9b6d"
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

		if (manager.world.isRemote) return;

		EffectMutationInstance mut = instanceMap.get(manager.getUniqueID());

		mut.getParent().getAttribute(Attributes.LUCK)
				.applyNonPersistentModifier(new AttributeModifier(uuid, "mutation.good_luck", 1.0D, AttributeModifier.Operation.ADDITION));
	}

	@Override
	public void deactivateMutation(PlayerManager manager) {
		super.deactivateMutation(manager);

		if (manager.world.isRemote) return;

		instanceMap.get(manager.getUniqueID()).getParent()
				.getAttribute(Attributes.LUCK)
				.removeModifier(uuid);
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BAD_LUCK);
	}
}