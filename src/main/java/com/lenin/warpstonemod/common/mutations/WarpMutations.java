package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.lenin.warpstonemod.common.mutations.attributes.AttrHarvestSpeed;
import com.lenin.warpstonemod.common.mutations.attributes.VanillaAttribute;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.InvisibilityMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.PpMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;

public enum WarpMutations {
    MAX_HEALTH,
    DAMAGE,
    SPEED,
    ARMOUR,
    ARMOUR_TOUGHNESS,
    MINING_SPEED;

    public static final String nameConst = "mutation.";

    public static Mutation constructAttributeMutation (WarpMutations type, LivingEntity parent) {
        switch (type){
            case MAX_HEALTH:
                return new AttributeMutation(parent, new VanillaAttribute(Attributes.MAX_HEALTH, parent), nameConst + "attribute." + Attributes.MAX_HEALTH.getAttributeName(), "358c0d76-adac-416a-ab2a-5e29b3bad0c6");
            case DAMAGE:
                return new AttributeMutation(parent, new VanillaAttribute(Attributes.ATTACK_DAMAGE, parent), nameConst + "attribute." + Attributes.ATTACK_DAMAGE.getAttributeName(), "d62d20f0-e41b-4f20-a82c-13367c07385c");
            case SPEED:
                return new AttributeMutation(parent, new VanillaAttribute(Attributes.MOVEMENT_SPEED, parent), nameConst + "attribute." + Attributes.MOVEMENT_SPEED.getAttributeName(), "721d5526-7b15-4f43-96d0-dafcccae4f3f");
            case ARMOUR:
                return new AttributeMutation(parent, new VanillaAttribute(Attributes.ARMOR, parent), nameConst + "attribute." + Attributes.ARMOR.getAttributeName(), "52b5a810-16ed-4696-a3d0-a05ceb8922d3");
            case ARMOUR_TOUGHNESS:
                return new AttributeMutation(parent, new VanillaAttribute(Attributes.ARMOR_TOUGHNESS, parent), nameConst + "attribute." + Attributes.ARMOR_TOUGHNESS.getAttributeName(), "32f6110f-238d-4840-b9ed-f00ccb3d32c2");
            case MINING_SPEED:
                return new AttributeMutation(parent, new AttrHarvestSpeed(parent), nameConst + "attribute." + Attributes.ATTACK_SPEED.getAttributeName(), "380312ba-82a6-4b9e-b576-4c5d9e2b1a32");
            default:
                return null;
        }
    }

    /*public enum EffectMutations {
        INVISIBILITY(0),
        PP(1);

        private int value;
        private static Map map = new HashMap<>();

        EffectMutations(int value){
            this.value = value;
        }

        static {
            for (EffectMutations e : EffectMutations.values()) {
                map.put(e.value, e);
            }
        }

        public static EffectMutations valueOf (int num){
            return (EffectMutations) map.get(num);
        }

        public int getValue(){
            return value;
        }

        public static EffectMutation constructEffectMutation(EffectMutations type, LivingEntity parent) {
            switch (type){
                case INVISIBILITY:
                    return new InvisibilityMutation(parent);
                case PP:
                    return new PpMutation(parent);
                default:
                    return null;
            }
        }
    }*/
}