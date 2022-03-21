package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AttrMeleeDamage extends WSAttribute {
    public AttrMeleeDamage(LivingEntity _parentEntity) {
        super(_parentEntity, "melee_damage");
    }

    @Override
    protected void attachListeners(IEventBus bus) {
        bus.addListener(this::onLivingHurt);
    }

    public void onLivingHurt (LivingHurtEvent event) {
        if (event.getEntity().world.isRemote()
                || event.getSource().getTrueSource() != parentEntity
        ) return;

        if (!event.getSource().isProjectile() && !(event.getSource() instanceof IndirectEntityDamageSource)) {
            float damage = event.getAmount() * getAttributeValue();
            event.setAmount(damage);
        }
    }
}