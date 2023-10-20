package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AttrRangedDamage extends WSAttribute {
    public AttrRangedDamage(LivingEntity _parentEntity) {
        super(_parentEntity, new ResourceLocation(Warpstone.MOD_ID, "ranged_damage"));
    }

    @Override
    protected void attachListeners(IEventBus bus) {
        bus.addListener(this::onLivingHurt);
    }

    public void onLivingHurt (LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()
                || event.getSource().getEntity() != parentEntity
        ) return;

        if (event.getSource().isProjectile()) {
            float damage = event.getAmount() * getAttributeValue();
            event.setAmount(damage);
        }
    }
}