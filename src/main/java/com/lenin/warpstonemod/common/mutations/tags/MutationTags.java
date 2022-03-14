package com.lenin.warpstonemod.common.mutations.tags;

import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;

public class MutationTags {

    public static MutationTag NEGATIVE = registerTag("negative");
    public static MutationTag POSITIVE = registerTag("positive");
    public static MutationTag DAMAGE = registerTag("damage");
    public static MutationTag COMMON = registerTag("common", Rarity.COMMON.color).addType(MutationTag.Type.RARITY);
    public static MutationTag UNCOMMON = registerTag("uncommon", Rarity.UNCOMMON.color).addType(MutationTag.Type.RARITY);
    public static MutationTag RARE = registerTag("rare", Rarity.RARE.color).addType(MutationTag.Type.RARITY);

    public static MutationTag registerTag(String key) {
        return new MutationTag(key);
    }

    public static MutationTag registerTag(String key, TextFormatting formatting) {
        return new MutationTag(key, formatting);
    }

    public static void init() {}
}