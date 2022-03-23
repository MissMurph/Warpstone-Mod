package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttributes;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ArcherMutation extends EffectMutation {
	public ArcherMutation() {
		super(
                "archer",
				"d35e4fe7-73bc-4fb4-97fe-b47a0e6cf62c"
		);

		modifiers.put(WSAttributes.RANGED_DAMAGE.getKey(), new AttributeModifier(
				uuid,
				mutName,
				0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));

		modifiers.put(WSAttributes.MELEE_DAMAGE.getKey(), new AttributeModifier(
				uuid,
				mutName,
				-0.25f,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		));
	}

	@Override
	public void attachListeners(IEventBus bus) {
		//bus.addListener(this::onLivingHurt);
	}

	/**The method here is to determine if the {@link net.minecraft.util.DamageSource} is from a projectile, and if not
	 * then process of elimination tells us it must either be a melee attack or another indirect source, if it's an
	 * indirect source then it isn't from a melee attack <br>
	 * <br>
	 * If the source is null it's not a melee attack <br>
	 * <br>
	 * TODO: Look into turning this into custom Attributes that we can modify
	 */

	public void onLivingHurt (LivingHurtEvent event) {
		if (event.getSource().getTrueSource() == null
				|| !containsInstance(event.getSource().getTrueSource().getUniqueID())
				|| !getInstance(event.getSource().getTrueSource().getUniqueID()).isActive()
		) return;

		if (event.getSource().isProjectile()) {
			float damage = event.getAmount() + Math.round(event.getAmount() * 0.25f);
			event.setAmount(damage);
		} else if (!(event.getSource() instanceof IndirectEntityDamageSource)) {
			float damage = event.getAmount() - Math.round(event.getAmount() * 0.25f);
			event.setAmount(damage);
		}
	}

	@Override
	public boolean isLegalMutation(PlayerManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BRAWLER);
	}
}