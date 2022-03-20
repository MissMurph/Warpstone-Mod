package com.lenin.warpstonemod.common.mutations.attributes;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public interface IAttributeSource {
	float getAttributeValue();
	String getAttributeName();

	void applyModifier (AttributeModifier source);
	void removeModifier(UUID source);
}