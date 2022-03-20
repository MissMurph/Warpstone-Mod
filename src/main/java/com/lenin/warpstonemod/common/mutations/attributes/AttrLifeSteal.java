package com.lenin.warpstonemod.common.mutations.attributes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.UUID;

public class AttrLifeSteal extends WSAttribute {

    public AttrLifeSteal (LivingEntity _parentEntity) {
        super(_parentEntity, "life_steal");

        baseValue = 0;
    }


    @Override
    public void attachListeners(IEventBus bus) {
        bus.addListener(this::onLivingDamage);
    }

    public void onLivingDamage (LivingDamageEvent event) {
        if (event.getEntity().world.isRemote()
                || event.getSource().getTrueSource() != parentEntity
        ) return;

        int amount = Math.round(event.getAmount() * getAttributeValue());
        ((PlayerEntity) event.getSource().getTrueSource()).heal(amount);
    }
}