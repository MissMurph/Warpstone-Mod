package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AttributeMutation extends Mutation{
	private final IAttributeSource attributeSource;
	private final String name;

	public AttributeMutation(LivingEntity _parentPlayer, IAttributeSource _attributeSource, String _name, String _uuid) {
		super(_parentPlayer, _uuid);
		this.attributeSource = _attributeSource;
		name = _name;
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

	@Override
	public void clearMutation() {
		attributeSource.removeModifier(uuid);
	}

	@Override
	public String getMutationName() {
		return getMutationType();
	}

	@Override
	public String getMutationType() {
		return this.attributeSource.getAttributeName();
	}
}