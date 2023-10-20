package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

public class HoovesMutation extends Mutation implements IMutationTick {
	public HoovesMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
		) return;

		ModifiableAttributeInstance instance = player.getAttribute(Attributes.MOVEMENT_SPEED);

		if (player.isSprinting() && player.getMainHandItem() == ItemStack.EMPTY && player.getOffhandItem() == ItemStack.EMPTY) {
			if (instance.getModifier(uuid) != null) return;

			instance.addTransientModifier(
					new AttributeModifier(
							uuid,
							name + ".speed.boost",
							0.3f,
							AttributeModifier.Operation.MULTIPLY_TOTAL
					)
			);
		} else if (instance.getModifier(uuid) != null) {
			instance.removeModifier(uuid);
		}
	}
}