package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.client.gui.screens.CorruptedTomeScreen;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CorruptedTomeItem extends Item {

    public CorruptedTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote()) Minecraft.getInstance().displayGuiScreen(new CorruptedTomeScreen());

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}