package com.lenin.warpstonemod.common.mutations.attribute_mutations;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WSAttributes {

    private static final Map<ResourceLocation, WSAttribute.AttributeFactory<WSAttribute>> ATTRIBUTE_MAP = new HashMap<>();

    public static final WSAttribute.AttributeFactory<WSAttribute> HARVEST_SPEED = register("harvest_speed", AttrHarvestSpeed::new);
    public static final WSAttribute.AttributeFactory<WSAttribute>  HEALING = register("healing", AttrHealing::new);
    public static final WSAttribute.AttributeFactory<WSAttribute>  LIFE_STEAL = register("life_steal", AttrLifeSteal::new);
    public static final WSAttribute.AttributeFactory<WSAttribute>  MELEE_DAMAGE = register("melee_damage", AttrMeleeDamage::new);
    public static final WSAttribute.AttributeFactory<WSAttribute>  RANGED_DAMAGE = register("ranged_damage", AttrRangedDamage::new);

    public static WSAttribute.AttributeFactory<WSAttribute>  register (String key, WSAttribute.AttributeSupplier<WSAttribute> supplier) {
        ResourceLocation resource = new ResourceLocation(Warpstone.MOD_ID, key);
        WSAttribute.AttributeFactory<WSAttribute>  factory = new WSAttribute.AttributeFactory<>(supplier, resource);
        ATTRIBUTE_MAP.put(resource, factory);
        return factory;
    }

    public static WSAttribute.AttributeFactory<WSAttribute>  register (ResourceLocation key, WSAttribute.AttributeSupplier<WSAttribute> supplier) {
        WSAttribute.AttributeFactory<WSAttribute>  factory = new WSAttribute.AttributeFactory<>(supplier, key);
        ATTRIBUTE_MAP.put(key, factory);
        return factory;
    }

    public static WSAttribute createAttribute (ResourceLocation key, LivingEntity parentEntity) {
        if (!ATTRIBUTE_MAP.containsKey(key)) return null;
        return ATTRIBUTE_MAP.get(key).get(parentEntity);
    }

    public static void register() {}
}