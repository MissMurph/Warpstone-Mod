package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;

public abstract class WarpWidget extends Widget {

	public WarpWidget(int x, int y, int width, int height, ITextComponent title) {
		super(x, y, width, height, title);
	}

	public void renderToolTip (MatrixStack matrixStack, List<ITextProperties> textLines,int mouseX, int mouseY, int width, int height, FontRenderer font) {
		GuiUtils.drawHoveringText(matrixStack, textLines, mouseX, mouseY, width, height, -1, font);
	}

	public abstract void renderWarpToolTip (MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) ;


	public boolean contains (int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}