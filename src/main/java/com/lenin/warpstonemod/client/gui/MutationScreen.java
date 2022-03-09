package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.client.gui.components.ButtonComponent;
import com.lenin.warpstonemod.client.gui.components.IClickable;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.components.TextComponent;
import com.lenin.warpstonemod.common.mutations.AttributeMutation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class MutationScreen extends WSScreen {
	private static final ResourceLocation SCREEN_LOCATION = new ResourceLocation("warpstonemod", "textures/gui/mutation_screen.png");
	//private static final ResourceLocation EFFECT_RESOURCE = new ResourceLocation("warpstonemod", "textures/gui/warp_icons.png");

	private int guiLeft, guiTop;
	private final int xSize = 176;
	private final int ySize = 166;

	private List<WSElement> elements = new ArrayList<>();

	//Dont ever let me write UI code again

	@Override
	protected void init(){
		super.init();

		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		MutateManager clientManager = MutateHelper.getClientManager();

		elements.add(new WSElement.Builder(this.guiLeft + 132, guiTop + 144, 20, 18, this)
				.addComponent(new ImageComponent(Textures.MUT_OPEN_SCREEN_BUTTON))
				.addComponent(
						new ButtonComponent((onPress) -> Minecraft.getInstance().displayGuiScreen(
								new InventoryScreen(Minecraft.getInstance().player)),
								Textures.MUT_OPEN_SCREEN_BUTTON_HVRD))
				.build()
		);


			/*	Instability	*/
		/*elements.add(new WSText(
				//title above the widget
				this.guiLeft + 121,
				guiTop + 64,
				25,
				25,
				new TranslationTextComponent("mutation.screen.instability")
		));

		WSElement instabilityWidget = new WSText(
				//widget
				this.guiLeft + 137,
				this.guiTop + 80,
				25,
				25,
				new StringTextComponent(String.valueOf(clientManager.getInstabilityLevel())));

		List<ITextComponent> instabilityList = new ArrayList<>();
		TextFormatting color = clientManager.getInstabilityLevel() > 5 ? TextFormatting.RED : TextFormatting.WHITE;

		int instWither = clientManager.getInstabilityLevel() * 10 - 30;

		// TODO: Have referenced objects (corruption, instability, mutations) return their effectToolTips rather than hard keying here

		instabilityList.add(new TranslationTextComponent("mutation.screen.instabilityWidget").mergeStyle(TextFormatting.WHITE));
		instabilityList.add(new TranslationTextComponent("warpstone.screen.generic.level")
				.appendSibling(new StringTextComponent(" "))
				.appendSibling(new StringTextComponent(String.valueOf(clientManager.getInstabilityLevel()))
						.mergeStyle(color)));
		instabilityList.add(new TranslationTextComponent("warpstone.screen.generic.total")
				.appendSibling(new StringTextComponent(" "))
				.appendSibling(new StringTextComponent(String.valueOf(clientManager.getInstability())))
				.mergeStyle(TextFormatting.WHITE));
		if (instWither > 0) {
			instabilityList.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent("+" + instWither + "%").mergeStyle(TextFormatting.RED))
			);
		}

		instabilityWidget.addToolTips(instabilityList.toArray(new ITextComponent[0]));
		elements.add(instabilityWidget);

			/* Corruption	*/
		/*elements.add(new WSText(
				//title above the widget
				this.guiLeft + 121,
				guiTop + 64,
				25,
				25,
				new TranslationTextComponent("mutation.screen.corruption")
		));

		WSElement corruptionWidget = new WSText(
				//widget
				this.guiLeft + 117,
				this.guiTop + 102,
				25,
				25,
				new StringTextComponent(String.valueOf(clientManager.getInstabilityLevel())));

		List<ITextComponent> corruptionTooltip = new ArrayList<>();
		int corWither = clientManager.getCorruptionLevel() * 10;

		corruptionTooltip.add(new TranslationTextComponent("mutation.screen.corruption").mergeStyle(TextFormatting.WHITE));
		corruptionTooltip.add(new TranslationTextComponent("warpstone.screen.generic.level")
				.appendSibling(new StringTextComponent(" "))
				.appendSibling(new StringTextComponent(String.valueOf(clientManager.getCorruptionLevel())))
				.mergeStyle(TextFormatting.WHITE));
		corruptionTooltip.add(new TranslationTextComponent("warpstone.screen.generic.total")
				.appendSibling(new StringTextComponent(" "))
				.appendSibling(new StringTextComponent(String.valueOf(clientManager.getCorruption())))
				.mergeStyle(TextFormatting.WHITE));

		corruptionTooltip.add(new TranslationTextComponent("warpstone.screen.generic.next_level")
				.mergeStyle(TextFormatting.GRAY)
				.mergeStyle(TextFormatting.ITALIC)
				.appendSibling(new StringTextComponent(" "))
				.appendSibling(new StringTextComponent(String.valueOf(clientManager.getCorruptionToNextLevel())).mergeStyle(TextFormatting.WHITE))
		);

		if (corWither > 0) {
			corruptionTooltip.add(new TranslationTextComponent("warpstone.consumable.wither_risk")
					.appendSibling(new StringTextComponent(" "))
					.appendSibling(new StringTextComponent("-" + corWither + "%").mergeStyle(TextFormatting.GREEN))
			);
		}

		corruptionWidget.addToolTips(corruptionTooltip.toArray(new ITextComponent[0]));
		elements.add(corruptionWidget); */

		List<AttributeMutation> muts = clientManager.getAttributeMutations();
		List<String> effectMuts = clientManager.getEffectMutations();

		for (int i = 0; i < muts.size(); i++) {
			int level = muts.get(i).getMutationLevel();
			System.out.println(level);

			int levelMultiple = level < 0 ? 5 : 10;

			float maxLevel = levelMultiple * Math.min(5, clientManager.getCorruptionLevel() + 1);

			float maxFrame = levelMultiple * 5;

			int frame = Math.round(((float)(level) / maxLevel) * maxFrame) + 25;

			List<ITextComponent> attrTooltips = new ArrayList<>();

			attrTooltips.add((new TranslationTextComponent(muts.get(i).getMutationName())).mergeStyle(TextFormatting.WHITE));
			String levelText = "+";
			TextFormatting color = TextFormatting.GREEN;
			if (level < 25) { levelText = ""; color = TextFormatting.RED; }

			String strMaxLevel = (level < 25 ? "-" + 5 * clientManager.getCorruptionLevel() : "+" + 10 * clientManager.getCorruptionLevel()) + "%";

			attrTooltips.add((new StringTextComponent(levelText + (level - 25) + "%")).mergeStyle(color));
			attrTooltips.add(new StringTextComponent("")
					.appendSibling(new TranslationTextComponent("warpstone.screen.generic.max_level"))
					.appendSibling(new StringTextComponent(" " + strMaxLevel)
							.mergeStyle(color)
					)
			);

			WSElement attrBar = new WSElement.Builder(getGuiLeft() + 13 + (17 * i), getGuiTop() + 60, 7, 78, this)
					.addComponent(new ImageComponent(Textures.SHEET_MUT_ATTR_BAR.resolve(frame)))
					.addTooltips(attrTooltips.toArray(new ITextComponent[0]))
					.build();

			//System.out.println("X:" + attrBar.x + " Y:" + attrBar.y + " Width:" + attrBar.width + " Height:" + attrBar.height);

			elements.add(attrBar);
		}

		for (int i = 0; i < effectMuts.size(); i++) {
			int y = getGuiTop() + 10;
			int x = getGuiLeft() + 10 + (23 * i);
			if (i >= 7) {
				y += 23;
				x = getGuiLeft() + 10 + (23 * (i - 7));
			}

			List<ITextComponent> effectToolTips = new ArrayList<>();
			effectToolTips.add(EffectMutations.getMutation(effectMuts.get(i)).getMutationName().mergeStyle(TextFormatting.BOLD));
			effectToolTips.add(EffectMutations.getMutation(effectMuts.get(i)).getMutationDesc());

			elements.add(new WSElement.Builder(x, y, 18, 18, this)
					.addComponent(new ImageComponent(
							new RawTextureResource(EffectMutations.getMutation(effectMuts.get(i)).getTexture(), 18, 18, 0, 0)))
					.addTooltips(effectToolTips.toArray(new ITextComponent[0]))
					.build()
			);

			//elements.add(img);
		}

		elements.add(new WSElement.Builder(0,0, 64, 64, this)
				.addComponent(new TextComponent(new StringTextComponent("mouseX: " + 0 + " mouseY: " + 0)))
				.build()
		);
	}

	public MutationScreen(ITextComponent titleIn) {
		super(titleIn);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(SCREEN_LOCATION);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

		for (WSElement w : elements) {
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

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int click) {
		elements.stream()
				.forEach(element -> element.onClick(mouseX, mouseY, click));

		return super.mouseClicked(mouseX, mouseY, click);
	}
}