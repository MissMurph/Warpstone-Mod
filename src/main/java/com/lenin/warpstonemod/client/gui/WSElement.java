package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.client.gui.components.Component;
import com.lenin.warpstonemod.client.gui.components.HoveredElementComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.*;
import java.util.stream.Collectors;

public class WSElement extends AbstractGui {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float alpha = 1.0f;

    protected boolean isHovered;

    protected List<Component> components;

    protected WSScreen parentScreen;

    protected List<WSElement> childElements;

    public static Builder builder (int _x, int _y, int _width, int _height, WSScreen _parentScreen) {
        return new Builder(_x, _y, _width, _height, _parentScreen);
    }

    protected WSElement(Builder builder) {
        parentScreen = builder.parentScreen;
        x = builder.x + parentScreen.getGuiLeft();
        y = builder.y + parentScreen.getGuiTop();
        width = builder.width;
        height = builder.height;

        for (Layer layer : builder.layers.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList())) {
            childElements.addAll(layer.build());
        }

        components = builder.componentFactories
                .stream()
                .map(factory -> factory.build(this))
                .collect(Collectors.toList());
    }

    public void render (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //this.isHovered = ();

        if (this.isHovered && isWithinChildBounds(mouseX, mouseY) || isWithinBounds(mouseX, mouseY)) {
            this.isHovered = parentScreen.setHovered(this);
        } else {
            parentScreen.clearHovered(this);
        }

        for (Component c : components) {
            if (c instanceof IRenderable) {
                ((IRenderable) c).render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }

        for (WSElement element : childElements) {
            element.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    public void onHover (MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //renderToolTip(matrixStack, mouseX, mouseY);


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

    public List<WSElement> getChildren () {
        return childElements;
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

    public WSScreen getScreen () {
        return parentScreen;
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
        protected final int x;
        protected final int y;
        protected final int width;
        protected final int height;
        protected final WSScreen parentScreen;
        protected final List<Component.IFactory> componentFactories = new ArrayList<>();
        protected final Map<Integer, Layer> layers = new HashMap<>();

        protected Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen){
            x = _x;
            y = _y;
            width = _width;
            height = _height;
            parentScreen = _parentScreen;
        }

        public <C extends Component.IFactory> Builder addComponent (C factory) {
            componentFactories.add(factory);
            return this;
        }

        public Builder addChild (Builder builder) {
            return addChild(builder, 0);
        }

        public Builder addChild (Builder builder, int layer) {
            layers.computeIfAbsent(layer, Layer::new);
            layers.get(layer).addBuilder(builder);
            return this;
        }

        public WSElement build () {
            return new WSElement(this);
        }
    }
}