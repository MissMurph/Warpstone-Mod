package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeMutation{
	protected final IAttributeSource attributeSource;
	protected final String name;
	protected final UUID uuid;
	protected int mutationLevel;
	protected final MutateManager manager;

	public AttributeMutation(IAttributeSource _attributeSource, MutateManager _manager, String _name, String _uuid) {
		uuid = UUID.fromString(_uuid);
		this.attributeSource = _attributeSource;
		name = _name;
		mutationLevel = 0;
		manager = _manager;
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

		int maxPos = manager.getCorruptionLevel() * 10;
		int maxNeg = manager.getCorruptionLevel() * -5;

		if (mutationLevel > maxPos) mutationLevel = maxPos;
		if (mutationLevel < maxNeg) mutationLevel = maxNeg;

		addModifier();
	}

	public void changeLevel (int value){
		setLevel(mutationLevel + value);
	}

	public boolean canMutate (MutateManager manager) {
		return mutationLevel < manager.getCorruptionLevel() * 10 || mutationLevel > manager.getCorruptionLevel() * -5;
	}

	public void clearMutation() {
		attributeSource.removeModifier(uuid);
	}

	public String getMutationName() {
		return name;
	}

	public String getMutationType() {
		return this.attributeSource.getAttributeName();
	}

	public int getMutationLevel (){
		return mutationLevel;
	}
}