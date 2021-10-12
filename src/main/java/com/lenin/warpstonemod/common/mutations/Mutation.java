package com.lenin.warpstonemod.common.mutations;

import java.util.UUID;

public abstract class Mutation {
    protected int mutationLevel;
    protected UUID uuid;

    public Mutation (String _uuid){
        uuid = UUID.fromString(_uuid);
    }

    public abstract String getMutationType ();

    public abstract String getMutationName ();


}