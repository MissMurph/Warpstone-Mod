package com.lenin.warpstonemod.api;

import com.google.gson.JsonObject;
import com.lenin.warpstonemod.common.CommonProxy;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.Mutation.*;
import com.lenin.warpstonemod.common.mutations.effect_mutations.GenericMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class WarpstoneAPI {

    private static WarpstoneAPI instance = null;

    private final CommonProxy proxy;
    private final JSONBuffer buffer;
    private final Registration registration;

    private WarpstoneAPI (CommonProxy _proxy, JSONBuffer _buffer, Registration _registration) {
        proxy = _proxy;
        buffer = _buffer;
        registration = _registration;
    }

    public static WarpstoneAPI init (CommonProxy _proxy, JSONBuffer _buffer, Registration _registration) {
        if (instance != null) return null;

        instance = new WarpstoneAPI(_proxy, _buffer, _registration);

        instance.buffer.subscribe("mutations", WarpstoneAPI::mutationReload);
        instance.buffer.subscribe("mutations/tags", WarpstoneAPI::tagReload);
        instance.buffer.subscribe("mutations/trees", WarpstoneAPI::treeReload);

        return instance;
    }

    public static <M extends Mutation, B extends AbstractMutationDataBuilder<M>> M registerMutation (B builder) {
        if (instance == null) return null;

        M mut = builder.get();

        instance.registration.register(mut);
        instance.buffer.primeData(mut.getRegistryName().getPath(), builder.build());

        return mut;
    }

    private static void mutationReload (List<JsonObject> read) {
        if (instance == null) return;

        for (JsonObject json : read) {
            ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

            if (Registration.MUTATIONS.containsKey(key)) {
                Registration.MUTATIONS.getValue(key).deserialize(json);
            }
        }
    }

    //Need to refactor Tags to using API before we can complete this
    private static void tagReload (List<JsonObject> read) {
        if (instance == null) return;

        for (JsonObject json : read) {
            ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

            /*if (tagMap.containsKey(key)) {
                tagMap.get(key).deserialize(json);
            }*/
        }
    }

    private static void treeReload (List<JsonObject> read) {
        if (instance == null) return;

        for (JsonObject json : read) {
            ResourceLocation key = new ResourceLocation(json.get("key").getAsString());

            Mutation mut = Registration.MUTATIONS.getValue(key);

            if (mut instanceof EvolvingMutation) {
                ((EvolvingMutation) mut).loadTreeData(json);
            }
        }
    }
}