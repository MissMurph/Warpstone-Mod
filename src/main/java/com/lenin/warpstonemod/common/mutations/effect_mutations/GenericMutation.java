package com.lenin.warpstonemod.common.mutations.effect_mutations;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class GenericMutation extends EffectMutation {

    public GenericMutation(ResourceLocation _key) {
        super(_key);
    }

    @Override
    public List<ITextComponent> getToolTips() {
        List<ITextComponent> out = super.getToolTips();

        //Remove the description line
        out.remove(1);

        return out;
    }
}