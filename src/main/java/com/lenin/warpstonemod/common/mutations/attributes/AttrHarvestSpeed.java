package com.lenin.warpstonemod.common.mutations.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Map;
import java.util.UUID;

public class AttrHarvestSpeed implements IAttributeSource{
	private double baseValue;
	private double modValue;
	private LivingEntity parentEntity;

	private final Map<UUID, AttributeModifier> modMap = new Object2ObjectArrayMap<>();

	public AttrHarvestSpeed (LivingEntity _parentEntity) {
		baseValue = 1;
		modValue = 0;
		parentEntity = _parentEntity;

		if (!parentEntity.getEntityWorld().isRemote) this.attachListeners(MinecraftForge.EVENT_BUS);
	}

	protected void attachListeners (IEventBus eventBus){
		eventBus.addListener(this::onBreakSpeed);
	}

	private void onBreakSpeed (PlayerEvent.BreakSpeed event) {
		float speed = event.getNewSpeed();
		event.setNewSpeed(speed += (speed * getAttributeValue()));
	}

	@Override
	public double getAttributeValue() {
		return baseValue + modValue;
	}

	@Override
	public String getAttributeName() {
		return "attribute.name.generic.harvest_speed";
	}

	@Override
	public void applyModifier(AttributeModifier source) {
		AttributeModifier mod = modMap.putIfAbsent(source.getID(), source);
		if (mod != null) {
			throw new IllegalArgumentException("Modifier is already applied on this attribute!");
		}

		switch (source.getOperation()){
			case ADDITION:
				modValue += source.getAmount();
				break;
			case MULTIPLY_BASE:
				modValue += baseValue * source.getAmount();
				break;
			case MULTIPLY_TOTAL:
				modValue = modValue * (1 + source.getAmount());
				break;
		}

		modMap.put(source.getID(), source);
	}

	@Override
	public void removeModifier(UUID source) {
		AttributeModifier mod = modMap.get(source);

		switch (mod.getOperation()){
			case ADDITION:
				modValue -= mod.getAmount();
				break;
			case MULTIPLY_BASE:
				modValue -= baseValue * mod.getAmount();
				break;
			case MULTIPLY_TOTAL:
				modValue = modValue * (1 - mod.getAmount());
				break;
		}

		modMap.remove(source);
	}
}