package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.items.IWarpstoneConsumable;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.AttributeMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DummyPlayerManager extends PlayerManager {

	public DummyPlayerManager() {
		super();
	}

	@Override
	public void mutate(IWarpstoneConsumable item) { }

	@Override
	protected CompoundNBT serialize() {
		System.out.println("Dummy Manager is being Called!");
		return null;
	}

	@Override
	public void loadFromNBT(CompoundNBT nbt) { System.out.println("Dummy Manager is being Called!"); }

	@Override
	public void resetMutations(boolean death) { System.out.println("Dummy Manager is being Called!"); }

	@Override
	public List<AttributeMutation> getAttributeMutations() {
		System.out.println("Dummy Manager is being Called!");
		return new LinkedList<AttributeMutation>();
	}

	@Override
	public List<ResourceLocation> getEffectMutations() {
		System.out.println("Dummy Manager is being Called!");
		return new ArrayList<>();
	}

	@Override
	public LivingEntity getParentEntity() {
		System.out.println("Dummy Manager is being Called!");

		return parentEntity;
	}

	@Override
	public int getInstability() {
		System.out.println("Dummy Manager is being Called!");
		return 0;
	}

	@Override
	public CompoundNBT getMutData() {
		System.out.println("Dummy Manager is being Called!");
		return null;
	}
}