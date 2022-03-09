package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.client.gui.components.Component;
import com.lenin.warpstonemod.client.gui.components.IClickable;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class WSElement extends AbstractGui {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float alpha = 1.0f;

    protected boolean isHovered;

    protected List<ITextComponent> toolTips;

    protected List<Component> components;

    protected Screen parentScreen;

    protected WSElement(WSElement.Builder builder) {
        x = builder.x;
        y = builder.y;
        width = builder.width;
        height = builder.height;
        components = builder.components;
        toolTips = builder.toolTips;
        parentScreen = builder.parentScreen;
    }

    public void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (this.isHovered) {
            //System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
            //System.out.println("X: " + this.x + " Y: " + this.y);
            renderToolTip(matrixStack, mouseX, mouseY);
        }

        for (Component c : components) {
            if (c instanceof IRenderable) {
                ((IRenderable) c).render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void renderToolTip (MatrixStack matrixStack, int mouseX, int mouseY) {
        GuiUtils.drawHoveringText(matrixStack, toolTips, mouseX, mouseY, parentScreen.width, parentScreen.height, -1, Minecraft.getInstance().fontRenderer);
    }

    public void clearComponents (){
        components.clear();
    }

    public boolean isHovered () {
        return isHovered;
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

    public float getAlpha () {
        return alpha;
    }

    public <T extends Component> Component getComponent (Class<T> type) {
        for (Component c : components) {
            if (c.getClass() == type) {
                return c;
            }
        }

        return null;
    }

    public void onClick (double mouseX, double mouseY, int click) {
        components.stream()
                .filter(component -> component instanceof IClickable)
                .forEach(component -> ((IClickable) component).onClick(mouseX, mouseY, click));
    }

    public static class Builder {
        private final int x, y, width, height;
        private final Screen parentScreen;
        private final List<Component> components = new ArrayList<>();
        private final List<ITextComponent> toolTips = new ArrayList<>();

        public Builder (int _x, int _y, int _width, int _height, Screen _parentScreen){
            x = _x;
            y = _y;
            width = _width;
            height = _height;
            parentScreen = _parentScreen;
        }

        public <T extends Component> WSElement.Builder addComponent (T component) {
            components.add(component);
            return this;
        }

        public WSElement.Builder addTooltips (ITextComponent... lines) {
            Collections.addAll(toolTips, lines);
            return this;
        }

        public WSElement build () {
            WSElement element = new WSElement(this);
            components.forEach(component -> component.setParent(element));
            return element;
        }
    }
}