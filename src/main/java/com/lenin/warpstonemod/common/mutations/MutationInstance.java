package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.PlayerManager;

public class MutationInstance {
	protected final PlayerManager parent;

	public MutationInstance(PlayerManager _parent){
		parent = _parent;
	}

	public PlayerManager getParent (){
		return parent;
	}
}