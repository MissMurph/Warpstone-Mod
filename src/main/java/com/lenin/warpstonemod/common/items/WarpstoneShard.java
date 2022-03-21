package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WarpstoneShard extends Item implements IWarpstoneConsumable{
	public WarpstoneShard(Properties properties) {
		super(properties);
	}

	private final int corruptionValue = 50;

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
		if (worldIn.isRemote()) return super.onItemUseFinish(stack, worldIn, entity);

		PlayerManager m = MutateHelper.getManager(entity);

		if (m == null) m = MutateHelper.createManager(entity);

		m.mutate(this);

		return super.onItemUseFinish(stack, worldIn, entity);
	}

	@Override
	public int getCorruptionValue() {
		return corruptionValue;
	}

	@Override
	public boolean canBeConsumed() {
		return false;
	}
}
