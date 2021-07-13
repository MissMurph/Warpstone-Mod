package com.lenin.warpstonemod.common.mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.UUID;

public class Mutation {
    private static LivingEntity parentPlayer;
    private int mutationLevel;

    public Mutation (LivingEntity _parentPlayer){
        parentPlayer = _parentPlayer;
        mutationLevel = 0;

        addModifier();
    }

    public void addModifier () {

        //if (parentPlayer.getAttribute(Attributes.MAX_HEALTH).getModifier(UUID.fromString("358c0d76-adac-416a-ab2a-5e29b3bad0c6")) == null) {
            parentPlayer.getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(
                    new AttributeModifier(
                            UUID.fromString("358c0d76-adac-416a-ab2a-5e29b3bad0c6"),
                            "mutation.max_health",
                            20,
                            AttributeModifier.Operation.MULTIPLY_BASE));
        //}

        System.out.println(parentPlayer.getAttribute(Attributes.MAX_HEALTH).getValue());
    }
}