package com.lenin.warpstonemod.common.mutations.effect_mutations;

import com.lenin.warpstonemod.common.mutations.Mutation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class GenericMutation extends Mutation {

    protected GenericMutation(ResourceLocation _key) {
        super(_key);
    }

    public static AbstractBuilder<GenericMutation> builder (ResourceLocation key) {
        return new AbstractBuilder<GenericMutation>(key);
    }

    /*@Override
    public List<ITextComponent> getToolTips() {
        List<ITextComponent> out = super.getToolTips();

        //Remove the description line
        out.remove(1);

        return out;
    }*/

    public
}