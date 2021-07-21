package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class Mutation {
    private static LivingEntity parentPlayer;
    private final IAttributeSource attributeSource;
    private int mutationLevel;
    private final UUID uuid;
    private final String name;

    public Mutation (LivingEntity _parentPlayer, IAttributeSource _attributeSource, String _name, String _uuid){
        parentPlayer = _parentPlayer;
        attributeSource = _attributeSource;
        name = _name;

        //cap this from -25 to 50
        mutationLevel = 0;

        uuid = UUID.fromString(_uuid);

        clearModifier();
    }

    public void changeLevel (int value){
        mutationLevel += value;

        if (mutationLevel > 50) mutationLevel = 50;
        if (mutationLevel < -25) mutationLevel = -25;

        addModifier();
    }

    public void setLevel (int value) {
        mutationLevel = value;

        if (mutationLevel > 50) mutationLevel = 50;
        if (mutationLevel < -25) mutationLevel = -25;

        addModifier();
    }

    private void addModifier () {
        clearModifier();

        attributeSource.applyModifier(
                new AttributeModifier(
                        uuid,
                        name,
                        (double)mutationLevel / 100,
                        AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public void clearModifier () {
        attributeSource.removeModifier(uuid);
    }

    public int getMutationLevel (){
        return mutationLevel;
    }

    public String getMutationType () { return attributeSource.getAttributeName(); }
}