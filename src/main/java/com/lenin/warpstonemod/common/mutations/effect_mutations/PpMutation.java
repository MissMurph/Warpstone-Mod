package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.WarpMutations;
import net.minecraft.entity.LivingEntity;

public class PpMutation extends EffectMutation {
	protected PpMutation(LivingEntity _parentPlayer, int _mutationLevel) {
		super(_parentPlayer, EffectFactory.id, _mutationLevel,
				WarpMutations.nameConst + "effect.largepp",
				WarpMutations.nameConst + "effect.smallpp",
				"pp_icon.png",
				"ba2f092b-76d6-4d71-85ba-51becadb4d19");
	}

	@Override
	public void applyMutation() {}

	@Override
	public void clearMutation() {}

	public static class EffectFactory implements IEffectFactory {
		public EffectFactory (){}

		protected static int id;

		@Override
		public int getID() {
			return id;
		}

		@Override
		public void setID(int value) {
			id = value;
		}

		@Override
		public EffectMutation factory(LivingEntity parent, int _mutationLevel) {
			return new PpMutation(parent, _mutationLevel);
		}
	}
}