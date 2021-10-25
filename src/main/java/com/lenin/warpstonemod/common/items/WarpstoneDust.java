package com.lenin.warpstonemod.common.items;

import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class WarpstoneDust extends Item implements IWarpstoneConsumable{
	public WarpstoneDust(Properties properties) {
		super(properties);
	}

	protected final int corruptionValue = 20;

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
		if (worldIn.isRemote()) return super.onItemUseFinish(stack, worldIn, entity);

		MutateManager m = MutateHelper.getManager(entity);

		if (m == null) m = MutateHelper.createManager(entity);

		m.mutate(this);

		return super.onItemUseFinish(stack, worldIn, entity);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (worldIn == null) return;

		MutateManager m = MutateHelper.getClientManager();
		double chance = m.getWitherRisk(corruptionValue) * 100;

		IFormattableTextComponent text = new TranslationTextComponent("warpstone.consumable.wither_risk");
		TextFormatting color = chance >= 25 ? TextFormatting.RED : TextFormatting.WHITE;

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		tooltip.add(text
			.appendSibling(new StringTextComponent(" "))
			.appendSibling(new StringTextComponent(df.format(chance) + "%")
			.mergeStyle(color)));
	}

	@Override
	public int getCorruptionValue() {
		return corruptionValue;
	}

	@Override
	public boolean canBeConsumed() {
		return true;
	}
}