package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.mutations.AttributeMutation;
import com.lenin.warpstonemod.common.mutations.DummyMutateManager;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.MutateManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutations;
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
import org.w3c.dom.Text;

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

	//Dont ever let me write UI code again

	@Override
	protected void init(){
		super.init();
		widgets.clear();

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		MutateManager clientManager = MutateHelper.getClientManager();

		widgets.add(new InstabilityWidget(this.guiLeft + 137, guiTop + 80, 25, 25, clientManager.getInstabilityLevel(), clientManager.getInstability()));
		widgets.add(new TextWidget(this.guiLeft + 121, guiTop + 64, 25, 25, "instability"));

		widgets.add(new CorruptionWidget(this.guiLeft + 136, guiTop + 119, 25, 25, clientManager.getCorruptionLevel(), clientManager.getCorruption(), clientManager.getCorruptionToNextLevel()));
		widgets.add(new TextWidget(this.guiLeft + 117, guiTop + 102, 25, 25, "corruption"));

		Widget returnButton = new ReturnButton(this.guiLeft + 132, guiTop + 144, 20, 18, this);
		widgets.add(returnButton);
		addButton(returnButton);

		List<AttributeMutation> muts = clientManager.getAttributeMutations();
		List<String> effectMuts = clientManager.getEffectMutations();

		for (int i = 0; i < muts.size(); i++) {
			int level = muts.get(i).getMutationLevel();

			int levelMultiple = level < 0 ? 5 : 10;

			float maxLevel = levelMultiple * Math.min(5, clientManager.getCorruptionLevel() + 1);

			float maxFrame = levelMultiple * 5;

			int frame = Math.round(((float)(level) / maxLevel) * maxFrame) + 25;

			widgets.add(new AttributeBar(
					getGuiLeft() + 13 + (17 * i),
					getGuiTop() + 60,
					frame,
					level,
					clientManager.getCorruptionLevel(),
					muts.get(i).getMutationName()
			));
		}

		for (int i = 0; i < effectMuts.size(); i++) {
			int y = getGuiTop() + 10;
			int x = getGuiLeft() + 10 + (23 * i);
			if (i >= 7) {
				y += 23;
				x = getGuiLeft() + 10 + (23 * (i - 7));
			}
			widgets.add(new EffectWidget(x, y, 18, 18, EffectMutations.getMutation(effectMuts.get(i))));
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
		private final int level;
		private final int corruptionLevel;
		private final String attributeName;

		public AttributeBar(int x, int y, int _frame, int _level, int _corruptionLevel, String _attributeName) {
			super(x, y, 7, 78, new TranslationTextComponent("mutation_screen.attribute_bar"));
			width = 7;
			height = 78;
			attributeName = _attributeName;
			frame = _frame;
			level = _level + 25;
			corruptionLevel = _corruptionLevel + 1;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();
			list.add((new TranslationTextComponent(attributeName)).mergeStyle(TextFormatting.WHITE));
			String levelText = "+";
			TextFormatting color = TextFormatting.GREEN;
			if (level < 25) { levelText = ""; color = TextFormatting.RED; }

			String maxLevel = (level < 25 ? "-" + 5 * corruptionLevel : "+" + 10 * corruptionLevel) + "%";

			list.add((new StringTextComponent(levelText + (level - 25) + "%")).mergeStyle(color));
			list.add(new StringTextComponent("")
					.appendSibling(new TranslationTextComponent("warpstone.screen.generic.max_level"))
					.appendSibling(new StringTextComponent(" " + maxLevel)
							.mergeStyle(color)
					)
			);

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

			TextFormatting color = value > 5 ? TextFormatting.RED : TextFormatting.WHITE;

			int witherRisk = value * 10 - 30;

			list.add(new TranslationTextComponent("mutation.screen.instability").mergeStyle(TextFormatting.WHITE));
			list.add(new TranslationTextComponent("warpstone.screen.generic.level")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent(String.valueOf(value))
							.mergeStyle(color)));
			list.add(new TranslationTextComponent("warpstone.screen.generic.total")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent(String.valueOf(totalValue)))
					.mergeStyle(TextFormatting.WHITE));
			if (witherRisk > 0) {
				list.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
						.appendSibling(new StringTextComponent(" "))
						.appendSibling(new StringTextComponent("+" + witherRisk + "%").mergeStyle(TextFormatting.RED))
				);
			}

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontRenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(2, 2, 2);

			matrixStack.pop();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.BLACK;

			fontRenderer.drawText(matrixStack, new TranslationTextComponent(String.valueOf(value)).mergeStyle(color), this.x, this.y, 10);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}

		/*	CORRUPTION WIDGET	*/
	class CorruptionWidget extends WarpWidget {
		private final int value;
		private final int totalValue;
		private final int remainingValue;

		public CorruptionWidget(int x, int y, int width, int height, int _value, int _totalValue, int _remainingValue) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen.text_widget"));
			value = _value;
			totalValue = _totalValue;
			remainingValue = _remainingValue;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();

			int witherRisk = value * 10;

			list.add(new TranslationTextComponent("mutation.screen.corruption").mergeStyle(TextFormatting.WHITE));
			list.add(new TranslationTextComponent("warpstone.screen.generic.level")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent(String.valueOf(value)))
					.mergeStyle(TextFormatting.WHITE));
			list.add(new TranslationTextComponent("warpstone.screen.generic.total")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent(String.valueOf(totalValue)))
					.mergeStyle(TextFormatting.WHITE));

			list.add(new TranslationTextComponent("warpstone.screen.generic.next_level")
					.mergeStyle(TextFormatting.GRAY)
					.mergeStyle(TextFormatting.ITALIC)
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent(String.valueOf(remainingValue)).mergeStyle(TextFormatting.WHITE))
			);

			if (witherRisk > 0) {
				list.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
						.appendSibling(new StringTextComponent(" "))
						.appendSibling(new StringTextComponent("-" + witherRisk + "%").mergeStyle(TextFormatting.GREEN))
				);
			}

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			FontRenderer fontRenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			//matrixStack.scale(10, 10, 10);

			matrixStack.pop();

			TextFormatting color = value > 5 ? TextFormatting.DARK_RED : TextFormatting.BLACK;

			fontRenderer.drawText(matrixStack, new TranslationTextComponent(String.valueOf(value)).mergeStyle(color), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}

		/*	Effect Widget	*/
	class EffectWidget extends WarpWidget {
		private final EffectMutation parent;

		public EffectWidget(int x, int y, int width, int height, EffectMutation _parent) {
			super(x, y, width, height, new TranslationTextComponent("mutation_screen." + _parent.getMutationName()));
			parent = _parent;
		}

		@Override
		public void renderWarpToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int width, int height, FontRenderer font) {
			List<ITextProperties> list = new ArrayList<>();

			list.add(parent.getMutationName());
			list.add(parent.getMutationDesc());

			renderToolTip (matrixStack, list, mouseX, mouseY, width, height, font);
		}

		@Override
		public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(parent.getTexture());
			RenderSystem.enableDepthTest();
			blit(matrixStack, x, y, 0, 0, this.width, this.height, 18, 18);
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
			FontRenderer fontRenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();

			matrixStack.push();

			matrixStack.scale(4, 4, 4);

			matrixStack.pop();

			fontRenderer.drawText(matrixStack, new TranslationTextComponent("mutation.screen." + value), this.x, this.y, 0);

			blit(matrixStack, x, y, width, height, 0, 0);
		}
	}
}