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
    protected LivingEntity parentPlayer;

    protected int mutationLevel;
    protected UUID uuid;

    public Mutation (LivingEntity _parentPlayer, String _uuid){
        parentPlayer = _parentPlayer;

        //cap this from -25 to 50
        //or from -1 to 1 for effect muts
        mutationLevel = 0;

        uuid = UUID.fromString(_uuid);
    }

    public abstract void changeLevel (int value);

    public abstract void setLevel (int value);

    public abstract void clearMutation ();

    public abstract String getMutationType ();

    public abstract String getMutationName ();

    public int getMutationLevel (){
        return mutationLevel;
    }


}