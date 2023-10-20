package com.lenin.warpstonemod.common.mutations.attribute_mutations.attributes;

import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.attribute_mutations.WSAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AttrHealing extends WSAttribute {

    public AttrHealing(LivingEntity _parentEntity) {
        super(_parentEntity, new ResourceLocation(Warpstone.MOD_ID, "healing"));
    }

    @Override
    protected void attachListeners(IEventBus bus) {
        bus.addListener(this::onLivingHeal);
    }

    protected void onLivingHeal (LivingHealEvent event) {
        if (event.getEntity().level.isClientSide()) return;

        float amount = event.getAmount() * getAttributeValue();
        event.setAmount(amount);
    }
}