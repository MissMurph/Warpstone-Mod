package com.lenin.warpstonemod.common.mutations.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.util.UUID;

public class VanillaAttribute implements IAttributeSource{
	private final ModifiableAttributeInstance attribute;

	public VanillaAttribute(Attribute _source, LivingEntity _parentEntity) {

		attribute = _parentEntity.getAttribute(_source);
	}

	@Override
	public double getAttributeValue() {
		return attribute.getValue();
	}

	public Attribute getAttributeSource() {
		return attribute.getAttribute();
	}

	@Override
	public String getAttributeName() {
		return attribute.getAttribute().getAttributeName();
	}

	@Override
	public void applyModifier(AttributeModifier source) {
		attribute.applyNonPersistentModifier(source);
	}

	@Override
	public void removeModifier(UUID source) {
		attribute.removeModifier(source);
	}
}