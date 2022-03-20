package com.lenin.warpstonemod.common.mutations.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.UUID;

public class AttrHarvestSpeed extends WSAttribute {
	public AttrHarvestSpeed (LivingEntity _parentEntity) {
		super(_parentEntity, "harvest_speed");
	}

	@Override
	protected void attachListeners (IEventBus bus){
		bus.addListener(this::onBreakSpeed);
	}

	private void onBreakSpeed (PlayerEvent.BreakSpeed event) {
		if (event.getPlayer() != parentEntity) return;

		float speed = event.getNewSpeed();
		event.setNewSpeed(speed + (speed * getAttributeValue()));
	}
}