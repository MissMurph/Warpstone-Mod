package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class WSAttributes {

    private static final Map<String, WSAttribute.AttributeSupplier<WSAttribute>> ATTRIBUTE_MAP = new HashMap<>();

    public static final WSAttribute HARVEST_SPEED = register("harvest_speed", AttrHarvestSpeed::new);
    public static final WSAttribute HEALING = register("healing", AttrHealing::new);
    public static final WSAttribute LIFE_STEAL = register("life_steal", AttrLifeSteal::new);
    public static final WSAttribute MELEE_DAMAGE = register("melee_damage", AttrMeleeDamage::new);
    public static final WSAttribute RANGED_DAMAGE = register("ranged_damage", AttrRangedDamage::new);

    public static WSAttribute register (String key, WSAttribute.AttributeSupplier<WSAttribute> supplier) {
        ATTRIBUTE_MAP.put(key, supplier);
        return supplier.get(null);
    }

    public static WSAttribute createAttribute (String key, LivingEntity parentEntity) {
        if (!ATTRIBUTE_MAP.containsKey(key)) return null;
        return ATTRIBUTE_MAP.get(key).get(parentEntity);
    }



    public static void register() {}
}