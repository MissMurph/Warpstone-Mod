package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.client.gui.components.Component;
import com.lenin.warpstonemod.client.gui.components.IClickable;
import com.lenin.warpstonemod.client.gui.elements.TileSheetElement;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WSElement extends AbstractGui {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float alpha = 1.0f;

    protected boolean isHovered;

    protected List<ITextComponent> toolTips = new ArrayList<>();

    protected List<Component> components = new ArrayList<>();

    protected WSScreen parentScreen;

    protected List<WSElement> childElements;
    protected final List<WSElement.AbstractBuilder<? extends WSElement>> childBuilders = new ArrayList<>();

    protected WSElement() {}

    public void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = (this.isHovered && isWithinChildBounds(mouseX, mouseY)) || isWithinBounds(mouseX, mouseY);

        if (this.isHovered) {
            parentScreen.setHovered(this);
        } else {
            parentScreen.clearHovered(this);
        }

        for (Component c : components) {
            if (c instanceof IRenderable) {
                ((IRenderable) c).render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void onHover (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderToolTip(matrixStack, mouseX, mouseY);

        for (WSElement element : childElements) {
            element.render(matrixStack, mouseX, mouseY, partialTicks);
            if (element.isWithinBounds(mouseX, mouseY)) element.onHover(matrixStack, mouseX, mouseY, partialTicks);
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

    public boolean isWithinBounds (int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    public boolean isWithinChildBounds (int mouseX, int mouseY) {
        for (WSElement element : childElements) {
            if (mouseX >= element.x && mouseY >= element.y && mouseX < element.x + element.width && mouseY < element.y + element.height) return true;
        }

        return false;
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

    public static class Builder extends AbstractBuilder<WSElement> {
        public Builder (int _x, int _y, int _width, int _height, WSScreen _parentScreen) {
            super(_x, _y, _width, _height, _parentScreen, new WSElement());
        }
    }

    public abstract static class AbstractBuilder<T extends WSElement> {
        protected T element;

        protected AbstractBuilder (int _x, int _y, int _width, int _height, WSScreen _parentScreen, T _element){
            element = _element;
            element.x = _x;
            element.y = _y;
            element.width = _width;
            element.height = _height;
            element.parentScreen = _parentScreen;
        }

        public <C extends Component> WSElement.AbstractBuilder<T> addComponent (C component) {
            component.setParent(element);
            element.components.add(component);
            return this;
        }

        public WSElement.AbstractBuilder<T> addTooltips (ITextComponent... lines) {
            Collections.addAll(element.toolTips, lines);
            return this;
        }

        public WSElement.AbstractBuilder<T> addChild (WSElement.AbstractBuilder<? extends WSElement> builder) {
            element.childBuilders.add(builder);
            return this;
        }

        public T build () {
            element.childElements = element.childBuilders.stream()
                    .map(AbstractBuilder::build)
                    .collect(Collectors.toList());

            element.x += element.parentScreen.getGuiLeft();
            element.y += element.parentScreen.getGuiTop();

            return element;
        }
    }
}