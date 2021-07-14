package com.lenin.warpstonemod.common.mutations;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.UUID;

public enum WarpMutations {
    MAX_HEALTH,
    DAMAGE,
    SPEED,
    ARMOUR,
    ARMOUR_TOUGHNESS;
    //MINING_SPEED;

    public static final String nameConst = "mutation.";

    public Mutation constructMutation (WarpMutations type, LivingEntity parent) {
        switch (type){
            case MAX_HEALTH:
                return new Mutation(parent, Attributes.MAX_HEALTH, nameConst + Attributes.MAX_HEALTH.getAttributeName(), "358c0d76-adac-416a-ab2a-5e29b3bad0c6");
            case DAMAGE:
                return new Mutation(parent, Attributes.ATTACK_DAMAGE, nameConst + Attributes.ATTACK_DAMAGE.getAttributeName(), "d62d20f0-e41b-4f20-a82c-13367c07385c");
            case SPEED:
                return new Mutation(parent, Attributes.MOVEMENT_SPEED, nameConst + Attributes.MOVEMENT_SPEED.getAttributeName(), "721d5526-7b15-4f43-96d0-dafcccae4f3f");
            case ARMOUR:
                return new Mutation(parent, Attributes.ARMOR, nameConst + Attributes.ARMOR.getAttributeName(), "52b5a810-16ed-4696-a3d0-a05ceb8922d3");
            case ARMOUR_TOUGHNESS:
                return new Mutation(parent, Attributes.ARMOR_TOUGHNESS, nameConst + Attributes.ARMOR_TOUGHNESS.getAttributeName(), "32f6110f-238d-4840-b9ed-f00ccb3d32c2");
            //case MINING_SPEED:
              //  attribute = Attributes.;
                //break;
            default:
                return null;
        }
    }
}