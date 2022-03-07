package com.lenin.warpstonemod.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class WSButton extends WSImage{

    public WSButton(int _x, int _y, int _width, int _height, ResourceLocation _texture, Button.IPressable onPress){
        super(_x, _y, _width, _height, _texture);
    }

    public void onPress (){}
}