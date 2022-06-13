package com.lenin.warpstonemod.common.items.warpstone_consumables;

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
	public int getCorruption() {
		return corruptionValue;
	}

	@Override
	public int getInstability() {
		return 0;
	}

	@Override
	public int getWither() {
		return 0;
	}

	@Override
	public boolean canBeConsumed() {
		return false;
	}
}
