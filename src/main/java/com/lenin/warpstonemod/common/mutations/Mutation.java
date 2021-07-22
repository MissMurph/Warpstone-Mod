package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public abstract class Mutation {
    protected static LivingEntity parentPlayer;

    protected int mutationLevel;
    protected final UUID uuid;
    protected final String name;

    public Mutation (LivingEntity _parentPlayer, String _name, String _uuid){
        parentPlayer = _parentPlayer;
        name = _name;

        //cap this from -25 to 50
        //or from -1 to 1 for effect muts
        mutationLevel = 0;

        uuid = UUID.fromString(_uuid);
    }

    public abstract void changeLevel (int value);

    public abstract void setLevel (int value);

    public abstract void clearMutation ();

    public int getMutationLevel (){
        return mutationLevel;
    }

    public String getMutationType () { return name; }
}