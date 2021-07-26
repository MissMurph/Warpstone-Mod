package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class InvisibilityMutation extends EffectMutation{
	protected InvisibilityMutation(LivingEntity _parentPlayer, int _id) {
		super(_parentPlayer, _id, WarpMutations.nameConst + "effect.invisibility", WarpMutations.nameConst + "effect.glowing", 0, "a2361e8f-1be0-478f-9742-a873400e9b6d");
		System.out.println("Invis Mutation Constructed");
		mutationLevel = 1;
	}

	@Override
	protected void applyMutation() {
		super.applyMutation();

		switch (mutationLevel) {
			case -1:
				parentPlayer.setGlowing(true);
				break;
			case 0:
				break;
			case 1:
				parentPlayer.setInvisible(true);
				break;
		}
	}

	@Override
	public void clearMutation() {
		super.clearMutation();

		parentPlayer.setInvisible(false);
		parentPlayer.setGlowing(false);
	}

	public static class EffectFactory implements IEffectFactory {
		public EffectFactory() { }

		private int id;

		@Override
		public int getID() {
			return this.id;
		}

		public void setID(int value){
			id = value;
		}

		@Override
		public EffectMutation factory(LivingEntity parent) {
			return new InvisibilityMutation(parent, id);
		}
	}
}