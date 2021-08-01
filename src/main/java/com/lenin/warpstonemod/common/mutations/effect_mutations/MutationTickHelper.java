package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraftforge.event.TickEvent;

import java.util.LinkedList;
import java.util.List;

public class MutationTickHelper {
	private static List<IMutationTick> mutations = new LinkedList<>();

	public static void onTick (TickEvent.PlayerTickEvent event){
		for (IMutationTick i : mutations) {
			i.onTick(event);
		}
	}

	public static void addListener (IMutationTick listener){
		mutations.add(listener);
	}
}