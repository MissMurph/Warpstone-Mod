package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.WSElement;
import net.minecraft.client.gui.AbstractGui;

public abstract class Component  extends AbstractGui {
    protected WSElement parentElement = null;
    
    public Component(){ }

    public void setParent (WSElement element) {
        if (parentElement == null) parentElement = element;
    }
}