package com.lenin.warpstonemod.common;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TickManager {

	private final Map<TickEvent.Type, List<ITickHandler>> handlers = new HashMap<>();

	public TickManager() {
		for (TickEvent.Type type : TickEvent.Type.values()) {
			if (type == TickEvent.Type.PLAYER || type == TickEvent.Type.WORLD) {
				handlers.put(type, new ArrayList<>());
			}
		}
	}

	public void attachListeners (IEventBus bus) {
		bus.addListener(this::onPlayerTick);
	}

	public void register (ITickHandler handler) {
		for (TickEvent.Type type : handler.getHandledTypes()) {
			handlers.get(type).add(handler);
		}
	}

	public void unregister (ITickHandler handler) {
		for (TickEvent.Type type : handler.getHandledTypes()) {
			handlers.get(type).remove(handler);
		}
	}

	private void onWorldTick (TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		for (ITickHandler handler : handlers.get(TickEvent.Type.WORLD)) {
			handler.onTick(TickEvent.Type.WORLD, event.world);
		}
	}

	private void onPlayerTick (TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		for (ITickHandler handler : handlers.get(TickEvent.Type.PLAYER)) {
			handler.onTick(TickEvent.Type.PLAYER, event.player, event.side);
		}
	}
}