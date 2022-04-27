package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.entity.LivingEntity;

public class MutationInstance {
	protected final PlayerManager parent;

	public MutationInstance(PlayerManager _parent){
		parent = _parent;
	}

	public PlayerManager getParent (){
		return parent;
	}
}