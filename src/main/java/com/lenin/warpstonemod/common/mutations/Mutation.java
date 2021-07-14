package com.lenin.warpstonemod.common.mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class Mutation {
    private static LivingEntity parentPlayer;
    private final Attribute attributeSource;
    private int mutationLevel;
    private final UUID uuid;
    private final String name;

    public Mutation (LivingEntity _parentPlayer, Attribute _attributeSource, String _name, String _uuid){
        parentPlayer = _parentPlayer;
        attributeSource = _attributeSource;
        name = _name;

        //cap this from -0.25 to 0.5
        mutationLevel = 0;

        uuid = UUID.fromString(_uuid);

        clearModifier();
    }

    public void changeLevel (double value){
        mutationLevel += value;

        if (mutationLevel > 50) mutationLevel = 50;
        if (mutationLevel < -25) mutationLevel = -25;

        addModifier();
    }

    private void addModifier () {
        clearModifier();

            parentPlayer.getAttribute(attributeSource).applyPersistentModifier(
                    new AttributeModifier(
                            uuid,
                            name,
                            (double)mutationLevel / 100,
                            AttributeModifier.Operation.MULTIPLY_BASE));

        System.out.println(parentPlayer.getAttribute(attributeSource).getValue());
    }

    public void clearModifier () {
        parentPlayer.getAttribute(attributeSource).removeModifier(uuid);
    }

    public double getMutationLevel (){
        return mutationLevel;
    }
}