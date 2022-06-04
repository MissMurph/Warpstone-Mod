package com.lenin.warpstonemod.client.gui.components;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolTipComponent extends Component implements IRenderable {

    protected List<ITextComponent> toolTips = new ArrayList<>();

    protected ToolTipComponent(WSElement _parentElement, ITextComponent... _toolTips) {
        super(_parentElement);

        Collections.addAll(toolTips, _toolTips);
    }

    public static IFactory factory (ITextComponent... toolTips) {
        return (screen) -> new ToolTipComponent(screen, toolTips);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (parentElement.isHovered()) {
            GuiUtils.drawHoveringText(matrixStack, toolTips, mouseX, mouseY, parentElement.getScreen().width, parentElement.getScreen().height, -1, Minecraft.getInstance().fontRenderer);
        }
    }
}