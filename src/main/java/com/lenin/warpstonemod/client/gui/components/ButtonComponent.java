package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.WSElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.SoundEvents;

public class ButtonComponent extends Component implements IClickable {
    protected ButtonComponent.IPressable onPress;

    public ButtonComponent(WSElement _parentElement, ButtonComponent.IPressable function) {
        super(_parentElement);
        onPress = function;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int click) {
        this.onPress.onPress();
    }

    @Override
    public boolean canClick(double mouseX, double mouseY, int click) {
        return parentElement.isHovered();
    }

    public interface IPressable {
        void onPress();
    }
}