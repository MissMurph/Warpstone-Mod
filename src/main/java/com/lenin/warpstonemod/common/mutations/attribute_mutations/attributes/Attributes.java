package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class Attributes {

    private static final Map<String, AttributeSupplier<WSAttribute>> ATTRIBUTE_MAP = new HashMap<>();

    public static final AttributeSupplier<WSAttribute> HARVEST_SPEED = register("harvest_speed", AttrHarvestSpeed::new);
    public static final AttributeSupplier<WSAttribute> HEALING = register("healing", AttrHealing::new);
    public static final AttributeSupplier<WSAttribute> LIFE_STEAL = register("life_steal", AttrLifeSteal::new);
    public static final AttributeSupplier<WSAttribute> MELEE_DAMAGE = register("melee_damage", AttrMeleeDamage::new);
    public static final AttributeSupplier<WSAttribute> RANGED_DAMAGE = register("ranged_damage", AttrRangedDamage::new);

    public static AttributeSupplier<WSAttribute> register (String key, AttributeSupplier<WSAttribute> supplier) {
        ATTRIBUTE_MAP.put(key, supplier);
        return supplier;
    }

    public static WSAttribute createAttribute (String key, LivingEntity parentEntity) {
        if (!ATTRIBUTE_MAP.containsKey(key)) return null;
        return ATTRIBUTE_MAP.get(key).get(parentEntity);
    }

    public interface AttributeSupplier<T extends WSAttribute> {
        T get(LivingEntity entity);
    }

    public static void register() {}
}