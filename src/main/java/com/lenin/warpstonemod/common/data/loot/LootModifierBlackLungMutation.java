package com.lenin.warpstonemod.common.data.loot;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
import net.minecraft.block.Block;
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

public class LootModifierBlackLungMutation extends LootModifier {
    protected LootModifierBlackLungMutation(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        for (LootParameter<?> p : LootParameterSets.BLOCK.getRequiredParameters()) {
            if (!context.has(p) || !context.has(LootParameters.THIS_ENTITY)) return generatedLoot;
        }

        PlayerManager manager = MutateHelper.getManager(context.get(LootParameters.THIS_ENTITY).getUniqueID());

        if (manager != null && manager.containsEffect(EffectMutations.BLACK_LUNG)) {
            Block block = context.get(LootParameters.BLOCK_STATE).getBlock();

            block.getTags().stream().filter(tag -> tag.equals(Tags.Blocks.ORES_COAL.getName())).forEach(tag -> {
                List<ItemStack> stack = new ArrayList<>(generatedLoot);

                for (ItemStack i : stack) {
                    if (WarpstoneMain.getRandom().nextBoolean()) generatedLoot.add(i);
                }
            });
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootModifierBlackLungMutation> {
        @Override
        public LootModifierBlackLungMutation read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new LootModifierBlackLungMutation(conditions);
        }

        @Override
        public JsonObject write(LootModifierBlackLungMutation instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}