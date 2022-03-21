package com.lenin.warpstonemod.common.mutations.attribute_mutations;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class VanillaAttribute implements IAttributeSource{
	private final ModifiableAttributeInstance attribute;

	public VanillaAttribute(Attribute _source, LivingEntity _parentEntity) {
		attribute = _parentEntity.getAttribute(_source);
	}

	@Override
	public float getAttributeValue() {
		return (float) attribute.getValue();
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