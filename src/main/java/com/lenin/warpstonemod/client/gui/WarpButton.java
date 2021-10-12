package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WarpButton extends Button {
	Screen parentGui;

	protected static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("warpstonemod","textures/gui/warp_icons.png");

	public WarpButton(int x, int y, int width, int height,  Screen _parentGui) {
		super(x, y, width, height, new TranslationTextComponent("Test Button"), Button::onPress);
		this.parentGui = _parentGui;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RESOURCE_LOCATION);

		int i = 0;
		if (this.isHovered()) {
			i += 19;
		}

		RenderSystem.enableDepthTest();
		blit(matrixStack, x, y, 0, (float)i, this.width, this.height, 256, 256);
	}

	public void setPosition (int _x, int _y){
		this.x = _x; this.y = _y;
	}

	protected int getParentLeft () {
		return (parentGui.width - 176) / 2;
	}

	protected int getParentTop () {
		return (parentGui.height - 166) / 2;
	}

	@Override
	public void onPress() {
		super.onPress();
	}
}