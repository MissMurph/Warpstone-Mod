package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutationInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;

public class LuckMutation extends EffectMutation {
	public LuckMutation (int _id) {
		super(_id,
				WarpMutations.nameConst + "effect.good_luck",
				WarpMutations.nameConst + "effect.bad_luck",
				"luck_icon.png",
				"a2361e8f-1be0-478f-9742-a873400e9b6d");
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

		if (mut.getMutationLevel() == 1) mut.getParent()
				.getAttribute(Attributes.LUCK)
				.applyNonPersistentModifier(new AttributeModifier(uuid, "mutation.good_luck", 1.0D, AttributeModifier.Operation.ADDITION));
		if (mut.getMutationLevel() == -1) mut.getParent()
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
}