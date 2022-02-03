package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterEffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import com.lenin.warpstonemod.common.mutations.effect_mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScarringMutation extends CounterEffectMutation implements IMutationTick {
	public ScarringMutation(int _id) {
		super(_id,
				"scarring",
				"50cc914e-dbfb-4d26-8be3-03de8151932a",
				Rarity.RARE,
				400
				);
	}

	private Map<UUID, Integer> bonusMap = new HashMap<>();

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
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

		if (deincrement(counterMap, player.getUniqueID())) {
			applyEffect(player);
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (event.getEntityLiving().world.isRemote()
				|| !containsInstance(event.getEntityLiving())
				|| !getInstance(event.getEntityLiving()).isActive()
			) return;

		UUID uuid = event.getEntityLiving().getUniqueID();

		int bonus = Math.min(7, Math.round(bonusMap.get(uuid) + (event.getAmount() / 2f)));
		bonusMap.put(uuid, bonus);

		for (int i = 0; i < event.getAmount() * 20; i++) {
			if (counterMap.get(uuid) > 100 && deincrement(counterMap, uuid)) {
				applyEffect(event.getEntityLiving());
				break;
			}
		}
	}

	private void applyEffect (LivingEntity entity) {
		entity.addPotionEffect(new EffectInstance(
				Effects.REGENERATION,
				60 + (bonusMap.get(entity.getUniqueID()) * 20)
		));
	}

	@Override
	public void applyMutation(LivingEntity entity) {
		super.applyMutation(entity);

		if (entity.world.isRemote()) return;

		bonusMap.put(entity.getUniqueID(), 0);
	}

	@Override
	public void deactivateMutation(LivingEntity entity) {
		super.deactivateMutation(entity);

		if (entity.world.isRemote()) return;

		bonusMap.remove(entity.getUniqueID());
	}

	@Override
	public boolean isLegalMutation(MutateManager manager) {
		return super.isLegalMutation(manager) && !manager.containsEffect(EffectMutations.TURTLE);
	}
}