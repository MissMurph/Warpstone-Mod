package com.lenin.warpstonemod.api;

import com.google.gson.JsonObject;

import java.util.List;

@FunctionalInterface
public interface IJSONSniffer {
    void accept(List<JsonObject> read);
}