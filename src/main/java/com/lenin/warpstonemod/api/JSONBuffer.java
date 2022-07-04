package com.lenin.warpstonemod.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.api.directory.Directory;
import com.lenin.warpstonemod.client.data.WarpBlockStateProvider;
import com.lenin.warpstonemod.client.data.WarpItemModelProvider;
import com.lenin.warpstonemod.common.data.WSRecipeProvider;
import com.lenin.warpstonemod.common.data.items.MutateItemDataProvider;
import com.lenin.warpstonemod.common.data.loot.WSLootModifierProvider;
import com.lenin.warpstonemod.common.data.loot.WSLootTableProvider;
import com.lenin.warpstonemod.common.data.mutations.MutationDataProvider;
import com.lenin.warpstonemod.common.data.mutations.MutationTreeDataProvider;
import com.lenin.warpstonemod.common.data.tags.MutationTagsProvider;
import com.lenin.warpstonemod.common.data.tags.WSBlockTagsProvider;
import com.lenin.warpstonemod.common.data.tags.WSItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.*;

public class JSONBuffer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, List<JsonObject>> outputMap = new HashMap<>();

    private static final Map<String, List<IJSONSniffer>> snifferMap = new HashMap<>();

    private final Map<String, Directory> directoryTree = new HashMap<>();

    private final String nameSpace;

    public JSONBuffer(String _nameSpace) {
        nameSpace = _nameSpace;
    }

    //These events happen in two different phases, very funky and most certainly fresh
    public void attachListener (IEventBus bus) {
        bus.addListener(this::onRegisterReloadListeners);
    }

    public void attachLifecycle (IEventBus bus) {
        bus.addListener(this::onGatherData);
    }

    public void primeData (String path, JsonObject json) {
        Directory dir = registerDirectory(path);

        dir.registerData(json);
    }

    public void subscribe (String path, IJSONSniffer sniffer) {
        Directory dir = registerDirectory(path);

        dir.registerSniffer(sniffer);
    }

    private void onRegisterReloadListeners (AddReloadListenerEvent event) {
        //event.addListener(jsonBuffer);
    }

    private void onGatherData (GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        //Models
        gen.addProvider(new WarpBlockStateProvider(gen, fileHelper));
        gen.addProvider(new WarpItemModelProvider(gen, fileHelper));

        //Tags
        WSBlockTagsProvider blockTags = new WSBlockTagsProvider(gen, fileHelper);
        gen.addProvider(blockTags);
        gen.addProvider(new WSItemTagsProvider(gen, blockTags, fileHelper));
        gen.addProvider(new MutationTagsProvider(gen));

        //Misc.
        gen.addProvider(new WSLootTableProvider(gen));
        gen.addProvider(new WSRecipeProvider(gen));
        gen.addProvider(new WSLootModifierProvider(gen));
        //gen.addProvider(new MutationDataProvider(gen));
        //gen.addProvider(new MutationTreeDataProvider(gen));
        gen.addProvider(new MutateItemDataProvider(gen));
    }

    private Directory registerDirectory (String path) {
        String[] pathArray = path.split("/");

        Directory current = directoryTree.computeIfAbsent(pathArray[0], key -> new Directory(pathArray[0], this, null));

        for (int i = 1; i < pathArray.length; i++) {
            if (current.getIndex() < i) {
                current = current.registerChild(pathArray[i]);
            }
        }

        return current;
    }

    public Gson getGson(){
        return GSON;
    }

    public String getNameSpace () {
        return nameSpace;
    }
}