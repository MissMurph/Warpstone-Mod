package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.mutations.AttributeMutation;
import com.lenin.warpstonemod.common.mutations.DummyMutateManager;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MutationScreen extends Screen {
	private static final ResourceLocation SCREEN_LOCATION = new ResourceLocation("warpstonemod", "textures/gui/mutation_screen.png");
	private static final ResourceLocation ATTRIBUTE_RESOURCE = new ResourceLocation("warpstonemod","textures/gui/mutation_attribute_bar.png");
	//private static final ResourceLocation EFFECT_RESOURCE = new ResourceLocation("warpstonemod", "textures/gui/warp_icons.png");

	private int guiLeft, guiTop;
	private final int xSize = 176;
	private final int ySize = 166;

	private List<Widget> widgets = new ArrayList<Widget>();

	@Override
	protected void init(){
		super.init();
		widgets.clear();

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		widgets.add(new TextWidget(this.guiLeft + 130, guiTop + 82, 25, 25, String.valueOf(MutateHelper.getClientManager().getInstability())));
		widgets.add(new TextWidget(this.guiLeft + 120, guiTop + 68, 25, 25, "Instability"));

		Widget returnButton = new ReturnButton(this.guiLeft + 132, guiTop + 125, 20, 18, this);
		widgets.add(returnButton);
		addButton(returnButton);

		if (MutateHelper.getClientManager() instanceof DummyMutateManager) System.out.println("Parent Entity is Null!");

		List<AttributeMutation> muts = MutateHelper.getClientManager().getAttributeMutations();
		List<EffectMutation> effectMuts = MutateHelper.getClientManager().getEffectMutations();

		for (int i = 0; i < muts.size(); i++) {
			widgets.add(new AttributeBar(getGuiLeft() + 13 + (17 * i), getGuiTop() + 60, muts.get(i).getMutationLevel() + 25, muts.get(i).getMutationName(), this));
		}

		for (int i = 0; i < effectMuts.size(); i++) {
			int y = getGuiTop() + 10;
			if (i > 7) y += 24;
			widgets.add(new EffectWidget(getGuiLeft() + 10 + (23 * i), y, 18, 18, effectMuts.get(i)));
		}
	}

	public MutationScreen(ITextComponent titleIn) {
		super(titleIn);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(SCREEN_LOCATION);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

		for (Widget w : widgets) {
			w.render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
			this.closeScreen();
			return true;
		}
		return true;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	public int getGuiLeft () { return guiLeft; }

	public int getGuiTop () { return guiTop; }



		/*	ATTRIBUTE BAR	*/
	class AttributeBar extends Widget {
		private final int frame;
		private final String attributeName;
		private final MutationScreen parentGui;

		public AttributeBar(int x, int y, int _frame, String _attributeName, MutationScreen _parentGui) {
			super(x, y, 7, 78, new TranslationTextComponent("mutation_screen.attribute_bar"));
			width = 7;
			height = 78;
			frame = _frame;
			attributeName = _attributeName;
			parentGui = _parentGui;
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(ATTRIBUTE_RESOURCE);

			int x2 = 0;
			int y2 = 0;

			if (frame < 25)  {
				x2 = frame;
				y2 = 0;
			}
			else if (frame >= 25 && frame < 57) {
				x2 = frame - 25;
				y2 = 1;
			}
			else if (frame >= 57) {
				x2 = frame - 57;
				y2 = 2;
			}

			RenderSystem.enableDepthTest();
			RenderSystem.depthFunc(0);
			blit(matrixStack, this.x, this.y, x2 * 8, y2 * 78, this.width, this.height, 256, 256);
		}
	}

		/*	TEXT WIDGET	*/
	class TextWidget extends Widget {
		private final String value;

		public TextWidget(int x, int y, int width, int height, String _value) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen.text_widget"));
			value = _value;
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(2, 2, 2);

			matrixStack.pop();

			fontrenderer.drawText(matrixStack, new TranslationTextComponent(value), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}

		/*	Effect Widget	*/
	class EffectWidget extends Widget {
		private final EffectMutation parent;

		public EffectWidget(int x, int y, int width, int height, EffectMutation _parent) {
			super(x, y, width, height, new TranslationTextComponent(
					"mutations_screen.effect." + _parent.getMutationName(_parent.getInstance(Minecraft.getInstance().player).getMutationLevel())));
			parent = _parent;
		}

		@Override
		public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(parent.getTexture());

			int i = 0;

			if (parent.getInstance(Minecraft.getInstance().player).getMutationLevel() == -1) i = 18;

			RenderSystem.enableDepthTest();
			blit(matrixStack, x, y, 0, (float)i, this.width, this.height, 18, 36);
		}
	}

		/*	SCREEN TOGGLE BUTTON	*/
	class ReturnButton extends WarpButton {
			public ReturnButton(int x, int y, int width, int height, Screen _parentGui) {
				super(x, y, width, height, _parentGui);
			}

			@Override
			public void onPress() {
				Minecraft.getInstance().displayGuiScreen(new InventoryScreen(Minecraft.getInstance().player));
			}
		}
}