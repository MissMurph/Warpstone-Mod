package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MutationReset extends Item {
	public MutationReset(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (worldIn.isRemote()) return super.onItemRightClick(worldIn, playerIn, handIn);

		MutateManager m = MutateHelper.getManager(playerIn);

		if (m != null) m.resetMutations(true); else System.out.println("Manager is null");

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}