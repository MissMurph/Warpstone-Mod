package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.CounterMutation;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScarringMutation extends CounterMutation implements IMutationTick {
	public ScarringMutation(ResourceLocation _key) {
		super(_key,
                400
		);
	}

	private final Map<UUID, Integer> bonusMap = new HashMap<>();

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onLivingDamage);
	}

	@Override
	public void mutationTick(PlayerEntity player, LogicalSide side) {
		if (side == LogicalSide.CLIENT
				|| !containsInstance(player)
			) return;

		if (decrement(counterMap, player.getUniqueID())) {
			applyEffect(player);
		}
	}

	public void onLivingDamage (LivingDamageEvent event) {
		if (event.getEntityLiving().world.isRemote()
				|| !containsInstance(event.getEntityLiving())
			) return;

		UUID playerUUID = event.getEntityLiving().getUniqueID();

		int bonus = Math.min(7, Math.round(bonusMap.get(playerUUID) + (event.getAmount() / 2f)));
		bonusMap.put(playerUUID, bonus);

		for (int i = 0; i < event.getAmount() * 20; i++) {
			if (counterMap.get(playerUUID) > 100 && decrement(counterMap, playerUUID)) {
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
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		bonusMap.put(manager.getUniqueId(), 0);
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (manager.getParentEntity().world.isRemote()) return;

		bonusMap.remove(manager.getUniqueId());
	}
}