package com.lenin.warpstonemod.api;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.Mutation.*;
import net.minecraft.util.ResourceLocation;

public class WarpstoneAPI {

    public static <B extends AbstractBuilder<? extends Mutation>> Mutation registerMutation (ResourceLocation key, B builder) {
        Mutation mut = Warpstone.getProxy().getRegistration().register(builder.get());
        JSONBuffer.primeData(key.getPath(), builder.build());

        return mut;
    }
}