package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.mutations.attributes.IAttributeSource;
import com.lenin.warpstonemod.common.network.WarpPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Mutation {
    protected int mutationLevel;
    protected UUID uuid;

    public Mutation (String _uuid){
        uuid = UUID.fromString(_uuid);
    }

    public abstract String getMutationType ();

    public abstract String getMutationName ();


}