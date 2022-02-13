package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;

public class BlackLungMutation extends EffectMutation {

    public BlackLungMutation(int _id) {
        super(_id,
                "black_lung",
                "24d24ca3-4072-45d6-a7f0-29085b4f77fd",
                Rarity.COMMON
        );
    }

    @Override
    public void attachListeners(IEventBus bus) {

    }

    @Override
    public void attachClientListeners(IEventBus bus) {

    }
}