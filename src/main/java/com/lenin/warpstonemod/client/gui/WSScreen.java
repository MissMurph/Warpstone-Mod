package com.lenin.warpstonemod.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class WSScreen extends Screen {

    protected List<WSElement> elements = new ArrayList<>();

    protected WSScreen(ITextComponent title) {
        super(title);
        elements.clear();
    }
}