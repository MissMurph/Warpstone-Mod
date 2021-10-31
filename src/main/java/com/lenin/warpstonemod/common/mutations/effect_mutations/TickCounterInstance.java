package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

/**To use this type of instance, override {@link EffectMutation}.getInstanceType <br>
 * and replace the EffectMutationInstance construction with this (or other) type
 */

public class TickCounterInstance extends EffectMutationInstance{
	public TickCounterInstance(LivingEntity _parent, int _tickInterval) {
		super(_parent);
		tickInterval = _tickInterval;
	}

	protected int tickInterval;
	protected int currentTick;

	//Will return TRUE if this deincrementation results in a reset, FALSE if deincrements normally
	public boolean deincrement () {
		currentTick--;

		if (currentTick <= 0) {
			currentTick = tickInterval;
			return true;
		}
		return false;
	}
}