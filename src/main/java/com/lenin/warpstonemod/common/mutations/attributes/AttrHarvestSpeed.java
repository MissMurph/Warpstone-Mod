package com.lenin.warpstonemod.common.mutations.attributes;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.*;

public class AttrHarvestSpeed implements IAttributeSource{
	private double baseValue;
	private double modValue;
	private LivingEntity parentEntity;

	private Map<UUID, Double> resultMap = new Object2ObjectArrayMap<>();
	private final Map<UUID, AttributeModifier> modMap = new Object2ObjectArrayMap<>();

	public AttrHarvestSpeed (LivingEntity _parentEntity) {
		baseValue = 1;
		modValue = 0;
		parentEntity = _parentEntity;

		this.attachListeners(MinecraftForge.EVENT_BUS);
	}

	protected void attachListeners (IEventBus eventBus){
		eventBus.addListener(this::onBreakSpeed);
	}

	private void onBreakSpeed (PlayerEvent.BreakSpeed event) {
		if (event.getPlayer() != parentEntity) return;

		float speed = event.getNewSpeed();
		event.setNewSpeed((float) (speed + (speed * getAttributeValue())));
		System.out.println("Old Speed: " + speed + "New Speed: " + event.getNewSpeed());
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

		double resultValue;
		switch (source.getOperation()){
			case ADDITION:
				resultValue = source.getAmount() / 100;
				resultMap.put(source.getID(), resultValue);
				modValue += resultValue;
				break;

			case MULTIPLY_BASE:
				resultValue = baseValue * source.getAmount();
				resultMap.put(source.getID(), resultValue);
				modValue += resultValue;
				break;

			case MULTIPLY_TOTAL:
				resultValue = (baseValue + modValue) * source.getAmount();
				resultMap.put(source.getID(), resultValue);
				modValue += resultValue;
				break;
		}

		modMap.put(source.getID(), source);
	}

	@Override
	public void removeModifier(UUID source) {
		if (resultMap.get(source) == null) return;

		modValue -= resultMap.get(source);

		modMap.remove(source);
		resultMap.remove(source);
	}
}