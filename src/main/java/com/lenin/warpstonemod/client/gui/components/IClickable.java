package com.lenin.warpstonemod.client.gui.components;

public interface IClickable {
    void onClick (double mouseX, double mouseY, int click);
    boolean canClick (double mouseX, double mouseY, int click);
}