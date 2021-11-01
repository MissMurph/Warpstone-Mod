package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.entity.LivingEntity;

/**To use this type of instance, override {@link EffectMutation#getInstanceType(LivingEntity)} <br>
 * and replace the {@link EffectMutationInstance} construction with this (or other) sub-type
 */

public class TickCounterInstance extends EffectMutationInstance{
	public TickCounterInstance(LivingEntity _parent, int _tickInterval) {
		super(_parent);
		tickInterval = _tickInterval;
	}

	protected int tickInterval;
	protected int currentTick;

	public int getTick () {
		return currentTick;
	}

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