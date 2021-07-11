package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.WarpstoneMain;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class WarpButton extends Button {
	ContainerScreen parentGui;

	public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(WarpstoneMain.MOD_ID.toString(),"textures/gui/testbutton.png");

	public WarpButton(int x, int y, int width, int height,  ContainerScreen _parentGui) {
		super(x, y, width, height, new TranslationTextComponent("Test Button"), Button::onPress);
		this.parentGui = _parentGui;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RESOURCE_LOCATION);

		//if (this.isHovered()) {
			//i += this.yDiffText;
		//}

		RenderSystem.enableDepthTest();
		blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 256, 256);
		//if (this.isHovered()) {
		//	this.renderToolTip(matrixStack, mouseX, mouseY);
		//}
	}

	@Override
	public void onPress() {
		System.out.println("Test Button Pressed");
	}
}