package com.lenin.warpstonemod.client.gui;

import net.minecraft.util.ResourceLocation;

public class RawTextureResource {
    public final ResourceLocation resource;
    public final int sourceSizeX, sourceSizeY;
    public final int posX, posY;

    public RawTextureResource(ResourceLocation _resource, int _sourceSizeX, int _sourceSizeY, int _posX, int _posY) {
        resource = _resource;
        sourceSizeX = _sourceSizeX;
        sourceSizeY = _sourceSizeY;
        posX = _posX;
        posY = _posY;
    }
}