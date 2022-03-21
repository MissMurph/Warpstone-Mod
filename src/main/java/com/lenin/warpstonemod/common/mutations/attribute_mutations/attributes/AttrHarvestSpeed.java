package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

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