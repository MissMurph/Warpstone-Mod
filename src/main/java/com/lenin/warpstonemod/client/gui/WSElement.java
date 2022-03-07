package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WSElement extends AbstractGui {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float alpha = 1.0f;

    protected boolean isHovered;

    protected List<ITextComponent> toolTips = new ArrayList<>();

    public WSElement(int _x, int _y, int _width, int _height) {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
    }

    public void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (this.isHovered) {
            System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
            System.out.println("X: " + this.x + " Y: " + this.y);
            renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    public void renderToolTip (MatrixStack matrixStack, int mouseX, int mouseY) {
        GuiUtils.drawHoveringText(matrixStack, toolTips, mouseX, mouseY, width, height, -1, Minecraft.getInstance().fontRenderer);
    }

    public boolean isHovered () {
        return isHovered;
    }

    public void addToolTips (ITextComponent... lines) {
        Collections.addAll(toolTips, lines);
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }
}