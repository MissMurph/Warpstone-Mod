package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.ITickHandler;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.EnumSet;

public class MutationTickHelper implements ITickHandler {

	public static final MutationTickHelper INSTANCE = new MutationTickHelper();
	private MutationTickHelper(){}

	@Override
	public void onTick(TickEvent.Type type, Object... context) {
		PlayerEntity entity = (PlayerEntity) context[0];
		LogicalSide side = (LogicalSide) context[1];

		for (int i = 0; i < WarpstoneMain.getEffectsMap().getMapSize(); i++) {
			EffectMutation mut = WarpstoneMain.getEffectsMap().getMap().get(i);
			if (mut instanceof IMutationTick) ((IMutationTick) mut).mutationTick(entity, side);
		}
	}

	@Override
	public EnumSet<TickEvent.Type> getHandledTypes() {
		return EnumSet.of(TickEvent.Type.PLAYER);
	}
}