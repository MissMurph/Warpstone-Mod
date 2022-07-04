package com.lenin.warpstonemod.api.directory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lenin.warpstonemod.api.IJSONSniffer;
import com.lenin.warpstonemod.api.JSONBuffer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory {
    private final String path;
    private final Map<String, Directory> children;
    private final Directory parent;
    private final int index;
    private final List<IJSONSniffer> sniffers = new ArrayList<>();

    private final JSONBuffer buffer;

    private DirectoryProvider listener;

    private final List<JsonObject> data = new ArrayList<>();

    public Directory (String _path, JSONBuffer _buffer, @Nullable Directory _parent) {
        path = _path;
        buffer = _buffer;
        parent = _parent;

        if (parent == null) {
            index = 0;
        }
        else {
            index = parent.getIndex() + 1;
        }

        children = new HashMap<>();
    }

    public int getIndex () {
        return index;
    }

    public Directory registerChild (String path) {
        return children.computeIfAbsent(path, key -> new Directory(path, buffer, this));
    }

    public Directory getChild (String path) {
        if (children.containsKey(path)) return children.get(path);
        return null;
    }

    public void registerData (JsonObject _data) {
        data.add(_data);
    }

    public void registerSniffer (IJSONSniffer _sniffer) {
        sniffers.add(_sniffer);
    }

    public void registerProvider (DataGenerator _generator) {
        _generator.addProvider(new DirectoryProvider(buffer.getGson(), _generator, path));

        for (Directory dir : children.values()) {
            dir.registerProvider(_generator);
        }
    }

    private class DirectoryProvider implements IDataProvider {
        protected final DataGenerator generator;
        protected final String name;
        protected final Gson gson;

        private DirectoryProvider (Gson _gson, DataGenerator _generator, String _name) {
            generator = _generator;
            name = _name;
            gson = _gson;
        }

        @Override
        public void act(DirectoryCache cache) throws IOException {
            for (JsonObject data : data) {
                IDataProvider.save(gson, cache, data, this.generator.getOutputFolder().resolve("data/" + buffer.getNameSpace() + "/" + path + ".json"));
            }
        }

        @Override
        public String getName() {
            return name;
        }
    }
}