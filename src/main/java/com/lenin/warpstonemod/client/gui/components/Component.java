package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.common.mutations.conditions.IMutationCondition;
import net.minecraft.client.gui.AbstractGui;

public abstract class Component  extends AbstractGui {
    protected final WSElement parentElement;
    
    protected Component(WSElement _parentElement) {
        parentElement = _parentElement;
    }

    //public void setParent (WSElement element) {
        //if (parentElement == null) parentElement = element;
    //}

    @FunctionalInterface
    public interface IFactory {
        Component build(WSElement parent);
    }
}