package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.mutations.AttributeMutation;
import com.lenin.warpstonemod.common.mutations.DummyMutateManager;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.WarpstoneMain;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

		widgets.add(new InstabilityWidget(this.guiLeft + 133, guiTop + 79, 25, 25, MutateHelper.getClientManager().getInstabilityLevel(), MutateHelper.getClientManager().getInstability()));
		widgets.add(new TextWidget(this.guiLeft + 121, guiTop + 64, 25, 25, "instability"));

		widgets.add(new CorruptionWidget(this.guiLeft + 133, guiTop + 121, 25, 25, MutateHelper.getClientManager().getCorruptionLevel(), MutateHelper.getClientManager().getCorruption()));
		widgets.add(new TextWidget(this.guiLeft + 121, guiTop + 103, 25, 25, "corruption"));

		Widget returnButton = new ReturnButton(this.guiLeft + 132, guiTop + 144, 20, 18, this);
		widgets.add(returnButton);
		addButton(returnButton);

		if (MutateHelper.getClientManager() instanceof DummyMutateManager) System.out.println("Parent Entity is Null!");

		List<AttributeMutation> muts = MutateHelper.getClientManager().getAttributeMutations();
		Map<Integer, Integer> effectMuts = MutateHelper.getClientManager().getEffectLevelsMap();

		for (int i = 0; i < muts.size(); i++) {
			widgets.add(new AttributeBar(getGuiLeft() + 13 + (17 * i), getGuiTop() + 60, muts.get(i).getMutationLevel() + 25, muts.get(i).getMutationName(), this));
		}

		List<Integer> idList = new ArrayList<>(effectMuts.keySet());

		for (int i = 0; i < idList.size(); i++) {
			int y = getGuiTop() + 10;
			if (i > 7) y += 24;
			widgets.add(new EffectWidget(getGuiLeft() + 10 + (23 * i), y, 18, 18, WarpstoneMain.getEffectsMap().effectMap.get(idList.get(i)), effectMuts.get(idList.get(i))));
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
			if (w instanceof WarpWidget) {
				WarpWidget w2 = (WarpWidget) w;
				if (w2.contains(mouseX, mouseY)) w2.renderWarpToolTip(matrixStack, mouseX, mouseY, width, height, Minecraft.getInstance().fontRenderer);
			}

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
	class AttributeBar extends WarpWidget{
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
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();
			list.add((new TranslationTextComponent(attributeName)).mergeStyle(TextFormatting.WHITE));
			String levelText = "+";
			if (frame < 25) levelText = "-";

			list.add((new StringTextComponent(levelText + frame + "%")).mergeStyle(TextFormatting.WHITE));

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
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

		/*	INSTABILITY WIDGET	*/
	class InstabilityWidget extends WarpWidget {
		private final int value;
		private final int totalValue;

		public InstabilityWidget(int x, int y, int width, int height, int _value, int _totalValue) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen.text_widget"));
			value = _value;
			totalValue = _totalValue;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.WHITE;

			list.add((new TranslationTextComponent("mutation.screen.instability.name")).mergeStyle(TextFormatting.WHITE));
			list.add((new TranslationTextComponent("Level: " + value)).mergeStyle(color));
			list.add((new TranslationTextComponent("Total: " + totalValue)).mergeStyle(TextFormatting.WHITE));

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(2, 2, 2);

			matrixStack.pop();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.BLACK;

			fontrenderer.drawText(matrixStack, new TranslationTextComponent(String.valueOf(value)).mergeStyle(color), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}

		/*	CORRUPTION WIDGET	*/
	class CorruptionWidget extends WarpWidget {
		private final int value;
		private final int totalValue;

		public CorruptionWidget(int x, int y, int width, int height, int _value, int _totalValue) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen.text_widget"));
			value = _value;
			totalValue = _totalValue;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.WHITE;

			list.add((new TranslationTextComponent("mutation.screen.corruption.name")).mergeStyle(TextFormatting.WHITE));
			list.add((new TranslationTextComponent("Level: " + value)).mergeStyle(color));
			list.add((new TranslationTextComponent("Total: " + totalValue)).mergeStyle(TextFormatting.WHITE));

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(2, 2, 2);

			matrixStack.pop();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.BLACK;

			fontrenderer.drawText(matrixStack, new TranslationTextComponent(String.valueOf(value)).mergeStyle(color), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}

		/*	Effect Widget	*/
	class EffectWidget extends WarpWidget {
		private final EffectMutation parent;
		private final int level;

		public EffectWidget(int x, int y, int width, int height, EffectMutation _parent, int _level) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen." + _parent.getMutationName(_level)));
			parent = _parent;
			level = _level;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();

			list.add((new TranslationTextComponent(parent.getMutationName(level))).mergeStyle(TextFormatting.WHITE));
			list.add((new TranslationTextComponent(parent.getMutationDesc(level))).mergeStyle(TextFormatting.WHITE));

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(parent.getTexture());

			int i = 0;

			if (level == -1) i = 18;

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

	/*	TEXT WIDGET	*/
	class TextWidget extends Widget {
		private final String value;

		public TextWidget(int x, int y, int width, int height, String _value) {
			super(x, y, width, height, new TranslationTextComponent("mutation.screen." + _value));
			value = _value;
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontrenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(4, 4, 4);

			matrixStack.pop();

			fontrenderer.drawText(matrixStack, new TranslationTextComponent("mutation.screen." + value), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}
}