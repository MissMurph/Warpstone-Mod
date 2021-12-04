package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class HoovesMutation extends EffectMutation implements IMutationTick {
	public HoovesMutation(int _id) {
		super(_id,
				"hooves",
				"eeb67c1e-ef30-4d9a-b4ed-5b4ea156274a",
				Rarity.RARE);
	}

	@Override
	public void attachListeners(IEventBus bus) {

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

		ModifiableAttributeInstance instance = player.getAttribute(Attributes.MOVEMENT_SPEED);

		if (player.isSprinting() && player.getHeldItemMainhand() == ItemStack.EMPTY && player.getHeldItemOffhand() == ItemStack.EMPTY) {
			if (instance.getModifier(uuid) != null) return;

			instance.applyNonPersistentModifier(
					new AttributeModifier(
							uuid,
							((TranslationTextComponent)getMutationName()).getKey() + ".speed.boost",
							0.3f,
							AttributeModifier.Operation.MULTIPLY_TOTAL
					)
			);
		} else if (instance.getModifier(uuid) != null) {
			instance.removeModifier(uuid);
		}
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager);
	}
}