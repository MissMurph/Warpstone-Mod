package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WarpstoneDust extends Item {
	public WarpstoneDust(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
		MutateManager m = MutateHelper.getManager(entity);

		if (m == null) { m = MutateHelper.createManager(entity); }
		else { m.mutate();}

		return super.onItemUseFinish(stack, worldIn, entity);
	}


}