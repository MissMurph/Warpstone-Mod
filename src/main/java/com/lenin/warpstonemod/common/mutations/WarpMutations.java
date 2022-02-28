package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.AttrHarvestSpeed;
import com.lenin.warpstonemod.common.mutations.attributes.VanillaAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;

public enum WarpMutations {
    MAX_HEALTH,
    DAMAGE,
    SPEED,
    ARMOUR,
    ARMOUR_TOUGHNESS,
    MINING_SPEED;

    public static final String nameConst = "mutation.";

    public static AttributeMutation constructAttributeMutation (WarpMutations type, MutateManager parent) {
        switch (type){
            case MAX_HEALTH:
                return new AttributeMutation(new VanillaAttribute(Attributes.MAX_HEALTH, parent.getParentEntity()), parent, nameConst + Attributes.MAX_HEALTH.getAttributeName(), "358c0d76-adac-416a-ab2a-5e29b3bad0c6");
            case DAMAGE:
                return new AttributeMutation(new VanillaAttribute(Attributes.ATTACK_DAMAGE, parent.getParentEntity()), parent, nameConst + Attributes.ATTACK_DAMAGE.getAttributeName(), "d62d20f0-e41b-4f20-a82c-13367c07385c");
            case SPEED:
                return new AttributeMutation(new VanillaAttribute(Attributes.MOVEMENT_SPEED, parent.getParentEntity()), parent, nameConst + Attributes.MOVEMENT_SPEED.getAttributeName(), "721d5526-7b15-4f43-96d0-dafcccae4f3f");
            case ARMOUR:
                return new AttributeMutation(new VanillaAttribute(Attributes.ARMOR, parent.getParentEntity()), parent, nameConst + Attributes.ARMOR.getAttributeName(), "52b5a810-16ed-4696-a3d0-a05ceb8922d3");
            case ARMOUR_TOUGHNESS:
                return new AttributeMutation(new VanillaAttribute(Attributes.ARMOR_TOUGHNESS, parent.getParentEntity()), parent, nameConst + Attributes.ARMOR_TOUGHNESS.getAttributeName(), "32f6110f-238d-4840-b9ed-f00ccb3d32c2");
            case MINING_SPEED:
                return new AttributeMutation(new AttrHarvestSpeed(parent.getParentEntity()), parent, nameConst + "attribute.mining_speed", "380312ba-82a6-4b9e-b576-4c5d9e2b1a32");
            default:
                return null;
        }
    }
}