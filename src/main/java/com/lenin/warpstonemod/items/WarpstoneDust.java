package com.lenin.warpstonemod.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
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

		return super.onItemUseFinish(stack, worldIn, entity);
	}
}