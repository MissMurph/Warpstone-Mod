package com.lenin.warpstonemod.common.mutations.tags;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class MutationTag {
    protected ResourceLocation resourceLocation;
    protected String key;

    protected TextFormatting formatting;

    protected MutationTag.Type type;

    public MutationTag (String _key, TextFormatting _formatting){
        this(_key);
        formatting = _formatting;
    }

    public MutationTag (String _key) {
        key = _key;
        resourceLocation = new ResourceLocation(WarpstoneMain.MOD_ID, _key);
    }

    public ResourceLocation getResource () {
        return resourceLocation;
    }

    public TextFormatting getFormatting () {
        return formatting;
    }

    public MutationTag.Type getType () {
        return type;
    }

    public MutationTag addType (MutationTag.Type _type) {
        type = _type;

        return this;
    }

    public enum Type {
        RARITY,
        BENEFIT
    }
}