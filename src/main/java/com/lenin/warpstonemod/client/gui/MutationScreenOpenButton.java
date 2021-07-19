package com.lenin.warpstonemod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.text.TranslationTextComponent;

public class MutationScreenOpenButton extends WarpButton {
	public MutationScreenOpenButton(int x, int y, int width, int height, Screen _parentGui) {
		super(x, y, width, height, _parentGui);
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(RESOURCE_LOCATION);

		if (parentGui instanceof InventoryScreen && x != getParentLeft() + 134 || y != getParentTop() + (166 / 2) - 22) {
			this.setPosition(getParentLeft() + 134, getParentTop() + (166 / 2) - 22);
		}

		int i = 0;
		if (this.isHovered()) {
			i += 19;
		}

		RenderSystem.enableDepthTest();
		blit(matrixStack, x, y, 0, (float)i, this.width, this.height, 256, 256);

	}

	@Override
	public void onPress() {
		Minecraft.getInstance().displayGuiScreen(new MutationScreen(new TranslationTextComponent("warpstonemod.mutation_screen")));
	}
}