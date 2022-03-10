package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
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

		int maxPos = Math.min((manager.getCorruptionLevel() + 1) * 10, 50);
		int maxNeg = Math.max((manager.getCorruptionLevel() + 1) * -5, -25);

		if (mutationLevel > maxPos) mutationLevel = maxPos;
		if (mutationLevel < maxNeg) mutationLevel = maxNeg;

		addModifier();
	}

	public void changeLevel (int value){
		setLevel(mutationLevel + value);
	}

	public boolean canMutate (MutateManager manager) {
		return mutationLevel < Math.min((manager.getCorruptionLevel() + 1) * 10, 50) || mutationLevel > Math.max((manager.getCorruptionLevel() + 1) * -5, -25);
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