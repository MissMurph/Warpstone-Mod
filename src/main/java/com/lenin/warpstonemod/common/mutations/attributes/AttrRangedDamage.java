package com.lenin.warpstonemod.common.mutations.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AttrRangedDamage extends WSAttribute {
    public AttrRangedDamage(LivingEntity _parentEntity) {
        super(_parentEntity, "ranged_damage");
    }

    @Override
    protected void attachListeners(IEventBus bus) {
        bus.addListener(this::onLivingHurt);
    }

    public void onLivingHurt (LivingHurtEvent event) {
        if (event.getEntity().world.isRemote()
                || event.getSource().getTrueSource() != parentEntity
        ) return;

        if (event.getSource().isProjectile()) {
            float damage = event.getAmount() * getAttributeValue();
            event.setAmount(damage);
        }
    }
}