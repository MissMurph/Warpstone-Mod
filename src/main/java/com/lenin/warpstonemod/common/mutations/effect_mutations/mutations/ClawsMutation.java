package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

public class ClawsMutation extends EffectMutation implements IMutationTick {
	public ClawsMutation() {
		super(
                "claws",
				"dde7cc2c-20b4-4fec-af34-0e41044b2587"
		);
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

		ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

		if (player.getHeldItemMainhand() == ItemStack.EMPTY && player.getHeldItemOffhand() == ItemStack.EMPTY) {
			float bonusDamage = Math.min(10f, Math.round(3f + (7f * (1f - (Math.max(0, player.getHealth() - 5) / player.getMaxHealth())))));

			if (attribute.getModifier(uuid) != null) {
				if (attribute.getModifier(uuid).getAmount() != bonusDamage) {
					attribute.removeModifier(uuid);
					attribute.applyNonPersistentModifier(
							new AttributeModifier(
									uuid,
									((TranslationTextComponent) getMutationName()).getKey() + ".damage.boost",
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
								((TranslationTextComponent)getMutationName()).getKey() + ".damage.boost",
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