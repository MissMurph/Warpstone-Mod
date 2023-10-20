package com.lenin.warpstonemod.common.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.WSFoods;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.weighted_random.WeightModifier;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutateItem extends Item {
    protected MutateProperties mutateProperties;

    public MutateItem() {
        super(new Item.Properties()
                .tab(Warpstone.MOD_GROUP)
                .food(WSFoods.MUTATE_ITEM)
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entity) {
        if (worldIn.isClientSide()) return super.finishUsingItem(stack, worldIn, entity);

        PlayerManager m = MutateHelper.getManager(entity);

        if (m == null) m = MutateHelper.createManager(entity);

        m.mutate(this);

        return super.finishUsingItem(stack, worldIn, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (worldIn == null) return;

        PlayerManager m = MutateHelper.getClientManager();
        double chance = m.getWitherRisk(mutateProperties.wither) * 100;

        IFormattableTextComponent text = new TranslationTextComponent("warpstone.consumable.wither_risk");
        TextFormatting color = chance >= 25 ? TextFormatting.RED : TextFormatting.WHITE;

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        tooltip.add(text
                .append(new StringTextComponent(" "))
                .append(new StringTextComponent(df.format(chance) + "%")
                        .withStyle(color)));
    }



    public void loadData (JsonObject json) {

    }

    public MutateProperties getMutateProperties () {
        return mutateProperties;
    }

    public static class MutateProperties {
        public final int corruption;
        public final int instability;
        public final int wither;

        private final Map<ResourceLocation, List<WeightModifier>> modifiers = new HashMap<>();

        private MutateProperties (JsonObject json) {
            corruption = json.get("corruption").getAsInt();
            instability = json.get("instability").getAsInt();
            wither = json.get("wither").getAsInt();

            JsonArray array = json.getAsJsonArray("modifiers");

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();

                List<WeightModifier> list = modifiers.computeIfAbsent(new ResourceLocation(obj.get("target").getAsString()), key -> new ArrayList<>());

                list.add(new WeightModifier(obj.get("value").getAsFloat(), WeightModifier.Operation.valueOf(obj.get("operation").getAsString())));
            }
        }

        public Map<ResourceLocation, List<WeightModifier>> getModifiers () {
            return modifiers;
        }
    }
}