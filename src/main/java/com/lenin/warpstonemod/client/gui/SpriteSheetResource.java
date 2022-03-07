package com.lenin.warpstonemod.client.gui;

import net.minecraft.util.ResourceLocation;

public class SpriteSheetResource {

    public final RawTextureResource resource;
    public final int maxFrames;

    protected final SpriteSheetResource.ISheetGetter onResolve;

    public SpriteSheetResource (RawTextureResource _resource, int _maxFrames, SpriteSheetResource.ISheetGetter _onResolve){
        resource = _resource;
        maxFrames = _maxFrames;
        onResolve = _onResolve;
    }

    public RawTextureResource resolve (int frame) {
        return this.onResolve.resolve(frame);
    }

    public interface ISheetGetter {
        RawTextureResource resolve(int frame);
    }
}