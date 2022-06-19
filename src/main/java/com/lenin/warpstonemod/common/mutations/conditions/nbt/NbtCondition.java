package com.lenin.warpstonemod.common.mutations.conditions.nbt;

import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import com.lenin.warpstonemod.common.mutations.Mutations;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutationInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class NbtCondition implements IMutationCondition {
    protected final ResourceLocation registryKey;
    protected final Mutation parent;
    protected final String nbtKey;
    protected final INBT nbt;

    protected NbtCondition (ResourceLocation _registryKey, ResourceLocation _targetMut, String _nbtKey, INBT _nbt) {
        registryKey = _registryKey;
        parent = Mutations.getMutation(_targetMut);
        nbtKey = _nbtKey;
        nbt = _nbt;
    }

    @Override
    public ITextComponent getTooltip() {
        TranslationTextComponent text = new TranslationTextComponent("condition.nbt." + nbtKey);
        text.appendString(": ");
        text.appendString(nbt.getString());

        if (parent.containsInstance(Minecraft.getInstance().player.getUniqueID())) {
            INBT current = ((EvolvingMutationInstance) parent.getInstance(Minecraft.getInstance().player.getUniqueID())).readData(nbtKey);

            text.appendString(" | Current: " + current.getString());
        }

        return text;
    }

    @Override
    public ResourceLocation getKey() {
        return registryKey;
    }

    @Override
    public boolean act(PlayerManager manager) {
        return ((EvolvingMutationInstance) parent.getInstance(manager.getUniqueId())).readData(nbtKey).equals(nbt);
    }
}
