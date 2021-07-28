package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public abstract class EffectMutation extends Mutation {
	protected final String posName, negName;
	protected final int id;
	protected ResourceLocation resourceLocation;

	protected EffectMutation(LivingEntity _parentPlayer, int _id, int _mutationLevel, String _posName, String _negName,  String resName, String _uuid) {
		super(_parentPlayer, _uuid);
		posName = _posName;
		negName = _negName;
		id = _id;
		mutationLevel = _mutationLevel;

		resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/" + resName);
	}

	public void applyMutation (){
		if (parentPlayer == null) return;

		clearMutation();
	}

	@Override
	public void changeLevel(int value) {
		mutationLevel = value;

		if (mutationLevel > 1) mutationLevel = 1;
		if (mutationLevel < -1) mutationLevel = -1;

		applyMutation();
	}

	@Override
	public void setLevel(int value) {
		mutationLevel = value;

		if (mutationLevel > 1) mutationLevel = 1;
		if (mutationLevel < -1) mutationLevel = -1;

		applyMutation();
	}

	@Override
	public void clearMutation() { }

	@Override
	public int getMutationLevel() {
		return super.getMutationLevel();
	}

	@Override
	public String getMutationName() {
		switch (mutationLevel) {
			case -1:
				return negName;
			case 1:
				return posName;
			default:
				return "null";
		}
	}

	public ResourceLocation getTexture () {
		return resourceLocation;
	}

	@Override
	public String getMutationType() {
		return String.valueOf(id);
	}

	public int getMutationID() {
		return id;
	}

	public interface IEffectFactory {
		int getID();
		void setID(int value);
		EffectMutation factory(LivingEntity parent, int mutationLevel);
	}
}