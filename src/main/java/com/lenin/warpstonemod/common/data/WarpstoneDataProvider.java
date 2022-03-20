package com.lenin.warpstonemod.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import java.io.IOException;

public abstract class WarpstoneDataProvider implements IDataProvider {

    protected final DataGenerator generator;
    protected final String name;
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public WarpstoneDataProvider (DataGenerator _generator, String _name) {
        generator = _generator;
        name = _name;
    }

    @Override
    public abstract void act(DirectoryCache cache) throws IOException;

    @Override
    public String getName() {
        return name;
    }
}