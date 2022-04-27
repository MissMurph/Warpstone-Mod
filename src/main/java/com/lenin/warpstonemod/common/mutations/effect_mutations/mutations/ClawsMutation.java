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

public class ClawsMutation extends Mutation implements IMutationTick {
	public ClawsMutation(ResourceLocation _key) {
		super(_key);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
		) return;

		ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

		if (player.getHeldItemMainhand() == ItemStack.EMPTY && player.getHeldItemOffhand() == ItemStack.EMPTY) {
			float bonusDamage = Math.min(10f, Math.round(3f + (7f * (1f - (Math.max(0, player.getHealth() - 5) / player.getMaxHealth())))));

			if (attribute.getModifier(uuid) != null) {
				if (attribute.getModifier(uuid).getAmount() != bonusDamage) {
					attribute.removeModifier(uuid);
					attribute.applyNonPersistentModifier(
							new AttributeModifier(
									uuid,
									name + ".damage.boost",
									bonusDamage,
									AttributeModifier.Operation.ADDITION
							)
					);
				}
			}
			else {
				attribute.applyNonPersistentModifier(
						new AttributeModifier(
								uuid,
								name + ".damage.boost",
								bonusDamage,
								AttributeModifier.Operation.MULTIPLY_TOTAL
						)
				);
			}
		}
		else if (attribute.getModifier(uuid) != null) {
			attribute.removeModifier(uuid);
		}
	}
}