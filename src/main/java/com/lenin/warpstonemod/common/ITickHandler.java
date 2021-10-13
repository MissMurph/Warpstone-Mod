package com.lenin.warpstonemod.common;

import net.minecraftforge.event.TickEvent;

import java.util.EnumSet;

public interface ITickHandler {
	void onTick(TickEvent.Type type, Object... context);

	public EnumSet<TickEvent.Type> getHandledTypes();
}