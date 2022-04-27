package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.ITickHandler;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.IMutationTick;
import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;

public class MutationTickHelper implements ITickHandler {

	public static final MutationTickHelper INSTANCE = new MutationTickHelper();
	private MutationTickHelper(){}

	@Override
	public void onTick(TickEvent.Type type, Object... context) {
		PlayerEntity entity = (PlayerEntity) context[0];
		LogicalSide side = (LogicalSide) context[1];

		for (Mutation mut : new ArrayList<>(Registration.EFFECT_MUTATIONS.getValues())) {
			if (mut instanceof IMutationTick) ((IMutationTick) mut).mutationTick(entity, side);
		}
	}
}