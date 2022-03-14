package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import net.minecraft.item.Rarity;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ArcherMutation extends EffectMutation {
	public ArcherMutation() {
		super(
                "archer",
				"d35e4fe7-73bc-4fb4-97fe-b47a0e6cf62c",
				MutationTags.UNCOMMON);
	}

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingHurt);
	}

	@Override
	public void attachClientListeners(IEventBus bus) {

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
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.BRAWLER);
	}
}