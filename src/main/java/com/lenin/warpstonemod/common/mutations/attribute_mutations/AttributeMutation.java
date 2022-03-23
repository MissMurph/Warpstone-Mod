package com.lenin.warpstonemod.common.mutations.attribute_mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.IAttributeSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeMutation{
	protected final IAttributeSource attributeSource;
	protected final String name;
	protected final UUID uuid;
	protected int mutationLevel;
	protected final PlayerManager manager;

	public AttributeMutation(IAttributeSource _attributeSource, PlayerManager _manager, UUID _uuid) {
		uuid = _uuid;
		this.attributeSource = _attributeSource;
		name = _attributeSource.getAttributeName().getPath();
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

	public boolean canMutate (PlayerManager manager) {
		return mutationLevel < Math.min((manager.getCorruptionLevel() + 1) * 10, 50) || mutationLevel > Math.max((manager.getCorruptionLevel() + 1) * -5, -25);
	}

	public void clearMutation() {
		attributeSource.removeModifier(uuid);
	}

	public String getMutationName() {
		return name;
	}

	public String getMutationType() {
		return name;
	}

	public int getMutationLevel (){
		return mutationLevel;
	}
}