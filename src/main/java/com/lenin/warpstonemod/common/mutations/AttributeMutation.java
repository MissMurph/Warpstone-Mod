package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeMutation{
	protected final IAttributeSource attributeSource;
	protected final String name;
	protected final UUID uuid;
	protected int mutationLevel;

	public AttributeMutation(IAttributeSource _attributeSource, String _name, String _uuid) {
		uuid = UUID.fromString(_uuid);
		this.attributeSource = _attributeSource;
		name = _name;
		mutationLevel = 0;
	}

	protected void addModifier () {
		clearMutation();

		attributeSource.applyModifier(
				new AttributeModifier(
						uuid,
						name,
						(double)mutationLevel / 100,
						AttributeModifier.Operation.MULTIPLY_TOTAL));
	}

	public void setLevel (int value) {
		mutationLevel = value;

		if (mutationLevel > 50) mutationLevel = 50;
		if (mutationLevel < -25) mutationLevel = -25;

		addModifier();
	}

	public void changeLevel (int value){
		mutationLevel += value;

		if (mutationLevel > 50) mutationLevel = 50;
		if (mutationLevel < -25) mutationLevel = -25;

		addModifier();
	}

	public void clearMutation() {
		attributeSource.removeModifier(uuid);
	}


	public String getMutationName() {
		return getMutationType();
	}

	public String getMutationType() {
		return this.attributeSource.getAttributeName();
	}

	public int getMutationLevel (){
		return mutationLevel;
	}
}