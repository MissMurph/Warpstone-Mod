package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.WSElement;
import net.minecraft.client.gui.AbstractGui;

public abstract class Component  extends AbstractGui {
    protected WSElement parentElement;
    
    public Component(WSElement _parentElement){
        parentElement = _parentElement;
    }
}