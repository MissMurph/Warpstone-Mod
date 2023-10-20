package com.lenin.warpstonemod.common.mutations.attribute_mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.lenin.warpstonemod.common.mutations.tags.MutationTags;
import com.lenin.warpstonemod.common.weighted_random.IWeightable;
import com.lenin.warpstonemod.common.weighted_random.WeightEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.UUID;

public class AttributeMutation implements IWeightable<AttributeMutation> {
	protected final IAttributeSource attributeSource;
	protected final String name;
	protected final UUID uuid;
	protected int mutationLevel;
	protected final PlayerManager manager;

	protected final MutationTag tag;

	public AttributeMutation(IAttributeSource _attributeSource, PlayerManager _manager) {
		uuid = UUID.randomUUID();
		this.attributeSource = _attributeSource;
		name = _attributeSource.getAttributeName().getPath();
		mutationLevel = 0;
		manager = _manager;

		tag = MutationTags.getTag(Warpstone.key(name));
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

	public void reset () {
		setLevel(0);
	}

	public void setLevel (INBT nbt) {
		if (nbt instanceof NumberNBT) {
			setLevel(((NumberNBT) nbt).getAsInt());
		}
	}

	protected void setLevel (int value) {
		mutationLevel = value;

		int maxPos = Math.min((manager.getCorruptionLevel() + 1) * 10, 50);
		int maxNeg = Math.max((manager.getCorruptionLevel() + 1) * -5, -25);

		if (mutationLevel > maxPos) mutationLevel = maxPos;
		if (mutationLevel < maxNeg) mutationLevel = maxNeg;

		addModifier();
	}

	//Positive is true
	public WeightEntry<AttributeMutation> getWeight (boolean posOrNeg) {
		//return mutationLevel < Math.min((manager.getCorruptionLevel() + 1) * 10, 50) || mutationLevel > Math.max((manager.getCorruptionLevel() + 1) * -5, -25);
		//return posOrNeg ? mutationLevel < Math.min((manager.getCorruptionLevel() + 1) * 10, 50) : mutationLevel > Math.max((manager.getCorruptionLevel() + 1) * -5, -25);

		if (posOrNeg && mutationLevel < Math.min((manager.getCorruptionLevel() + 1) * 10, 50)) {
			return new WeightEntry<>(this, 100, IntNBT.valueOf(1));
		}
		else if (mutationLevel > Math.max((manager.getCorruptionLevel() + 1) * -5, -25)) {
			return new WeightEntry<>(this, 100, IntNBT.valueOf(-1));
		}

		return null;
	}

	public void mutate (WeightEntry<AttributeMutation> result) {
		int change = (manager.getCorruptionLevel() > 0 ? Warpstone.getRandom().nextInt(manager.getCorruptionLevel()) + 1 : 1);

		if (result.getValue() instanceof NumberNBT) {
			 change *= ((NumberNBT) result.getValue()).getAsInt();
		}

		setLevel(mutationLevel + change);
	}

	public void clearMutation() {
		attributeSource.removeModifier(uuid);
	}

	public TranslationTextComponent getMutationName() {
		return new TranslationTextComponent("attribute." + name);
	}

	public String getName() {
		return name;
	}

	public int getMutationLevel (){
		return mutationLevel;
	}

	@Override
	public AttributeMutation get() {
		return this;
	}

	public MutationTag getTag () {
		return tag;
	}
}