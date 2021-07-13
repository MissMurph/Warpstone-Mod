package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class WarpstoneDust extends Item {
	public WarpstoneDust(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
		entity.addPotionEffect(new EffectInstance(Effects.HASTE, 100, 0));

		//NetworkHooks.open

		//Minecraft.getInstance().displayGuiScreen(new WarpScreen(new TranslationTextComponent("TEST COCK")));

		new Mutation(entity);

		return super.onItemUseFinish(stack, worldIn, entity);
	}


}