package com.lenin.warpstonemod.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.lenin.warpstonemod.client.gui.WSElement.*;

public class Layer {

    private final int id;
    private final List<Builder> builders = new ArrayList<>();

    public Layer(int _id) {
        id = _id;
    }

    public int getId () {
        return id;
    }

    public void addBuilder (Builder builder) {
        builders.add(builder);
    }

    public List<? extends WSElement> build () {
        return builders.stream()
                .map(Builder::build)
                .collect(Collectors.toList());
    }

    public List<Builder> getBuilders () {
        return builders;
    }
}