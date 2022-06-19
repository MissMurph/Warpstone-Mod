package com.lenin.warpstonemod.common.data.loot;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.Mutations;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LootModifierFortuneMutation extends LootModifier {
	protected LootModifierFortuneMutation(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		for (LootParameter<?> p : LootParameterSets.BLOCK.getRequiredParameters()) {
			if (!context.has(p) || !context.has(LootParameters.THIS_ENTITY)) return generatedLoot;
		}

		PlayerManager manager = MutateHelper.getManager(context.get(LootParameters.THIS_ENTITY).getUniqueID());

		if (manager != null && manager.containsMutation(Mutations.FORTUNE)) {
			Block block = context.get(LootParameters.BLOCK_STATE).getBlock();

			block.getTags().stream().filter(tag -> tag.equals(Tags.Blocks.ORES.getName())).forEach(tag -> {
				List<ItemStack> stack = new ArrayList<>(generatedLoot);

				for (ItemStack i : stack) {
					if (!(i.getItem() instanceof BlockItem) && Warpstone.getRandom().nextBoolean()) {
						generatedLoot.add(i);
					}
				}
			});
		}

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<LootModifierFortuneMutation> {
		@Override
		public LootModifierFortuneMutation read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			return new LootModifierFortuneMutation(conditions);
		}

		@Override
		public JsonObject write(LootModifierFortuneMutation instance) {
			return this.makeConditions(instance.conditions);
		}
	}
}