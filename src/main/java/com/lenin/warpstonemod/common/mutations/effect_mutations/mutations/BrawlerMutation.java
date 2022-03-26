package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class BrawlerMutation extends EffectMutation {
	public BrawlerMutation() {
		super(
                "brawler",
				"af1bc073-2f9f-471e-a165-9681cfe4700c"
		);

		modifiers.put(WSAttributes.RANGED_DAMAGE.getKey(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(WSAttributes.MELEE_DAMAGE.getKey(), new AttributeModifier(
				uuid,
				mutName,
				0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	/**
	 * This mutation gives +25% melee damage but -25% ranged damage
	 *
	 */

	@Override
	public void attachListeners(IEventBus bus) {
		//bus.addListener(this::onLivingHurt);
	}

	//Same method as Archer
	//TODO: Turn the below into attributes
	public void onLivingHurt (LivingHurtEvent event) {
		if (event.getSource().getTrueSource() == null
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
				|| !getInstance(event.getSource().getTrueSource().getUniqueID()).isActive()
		) return;

		if (event.getSource().isProjectile()) {
			float damage = event.getAmount() - Math.round(event.getAmount() * 0.25f);
			event.setAmount(damage);
		} else if (!(event.getSource() instanceof IndirectEntityDamageSource)) {
			float damage = event.getAmount() + Math.round(event.getAmount() * 0.25f);
			event.setAmount(damage);
		}
	}
}