package com.lenin.warpstonemod.client.gui.screens;

import com.lenin.warpstonemod.client.gui.Layer;
import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.WSElement.*;
import com.lenin.warpstonemod.client.gui.components.ChooseOptionalComponent;
import com.lenin.warpstonemod.client.gui.components.HoveredElementComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.components.ToolTipComponent;
import com.lenin.warpstonemod.client.gui.elements.TileSheetElement;
import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.Warpstone;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import com.lenin.warpstonemod.common.mutations.tags.MutationTag;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;
import java.util.stream.Collectors;

public class WSScreen extends Screen {

    protected final RawTextureResource screenResource;

    protected List<WSElement> elements = new ArrayList<>();

    protected Map<Integer, Layer> layers = new LinkedHashMap<>();

    protected final int sizeX, sizeY;
    protected int guiLeft, guiTop;

    protected WSElement currentlyHovered;

    protected WSScreen(ITextComponent title, RawTextureResource _screenResource, int _sizeX, int _sizeY) {
        super(title);
        sizeX = _sizeX;
        sizeY = _sizeY;
        screenResource = _screenResource;
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.sizeX) / 2;
        this.guiTop = (this.height - this.sizeY) / 2;

        elements.forEach(WSElement::clearComponents);
        elements.clear();

        List<Layer> sortedLayers = layers.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        for (Layer sortedLayer : sortedLayers) {
            elements.addAll(sortedLayer.build());
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(screenResource.resource);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.sizeX, this.sizeY);

        for (WSElement w : elements) {
            w.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        //if (currentlyHovered != null) currentlyHovered.onHover(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input mouseKey = InputMappings.getKey(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (WSElement element : this.elements) {
            element.onClick(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean setHovered (WSElement element) {
        if (currentlyHovered == null || currentlyHovered == element) {
            currentlyHovered = element;
            return true;
        }
        else return false;
    }

    public void clearHovered (WSElement element) {
        if (currentlyHovered == element) currentlyHovered = null;
    }

    protected void layer (Builder builder) {
        layer(builder, 0);
    }

    protected void layer (Builder builder, int index) {
        layers.computeIfAbsent(index, Layer::new);
        layers.get(index).addBuilder(builder);
    }

    public int getGuiLeft () { return guiLeft; }

    public int getGuiTop () { return guiTop; }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected Builder mutationElement (int x, int y, Mutation mutation) {
        Builder topElement = mutation instanceof EvolvingMutation ? evoMutationElement(x, y, (EvolvingMutation) mutation) :
                WSElement.builder(x, y, 18, 18, this)
                    .addComponent(ToolTipComponent.factory(mutation.getToolTips().toArray(new ITextComponent[0])))
                    .addComponent(ImageComponent.factory(new RawTextureResource(mutation.getTexture(), 18, 18, 0, 0)));

        Builder border = WSElement.builder(x-2, y-2, width + 4, height + 4, this)
                .addComponent(ImageComponent.factory(Textures.MUTATION_BORDER));

        MutationTag primaryTag = null;

        for (MutationTag tag : mutation.getTags()) {
            if (!tag.getFormatting().isEmpty() && primaryTag == null || Math.abs(tag.getWeight()) > Math.abs(primaryTag.getWeight())) {
                primaryTag = tag;
            }
        }
            //Adding Trim to border
        if (primaryTag != null) {
            for (TextFormatting format : primaryTag.getFormatting()) {
                if (format.isColor()) {
                    border.addChild(WSElement.builder(x - 2, y - 2, width + 2, height + 2, this)
                                    .addComponent(ImageComponent.factory(
                                            new RawTextureResource(
                                                    Warpstone.key("textures/gui/effect_mutations/borders/trim_" + format.getName() + ".png"),
                                                    24, 24, 0, 0
                                            ))),
                            0
                    );

                    break;
                }
            }
        }

            //Adding Highlight to border
        border.addComponent(HoveredElementComponent.factory(WSElement.builder(x-2, y-2, width + 2, height + 2, this)
                .addComponent(ImageComponent.factory(Textures.MUTATION_BORDER_HIGHLIGHT))
        ));

        return topElement;
    }

    protected Builder evoMutationElement (int x, int y, EvolvingMutation mutation) {
        Builder mutationBuilder = WSElement.builder(x, y, 18, 18, this);

        List<MutationTree.Node> tree = mutation.getChildNodes();
        RawTextureResource texture = new RawTextureResource(mutation.getTexture(), 18, 18, 0, 0);
        MutationTree.Node currentNode = null;
        List<ResourceLocation> legalOptionals = new ArrayList<>();

        if (mutation.containsInstance(Minecraft.getInstance().player.getUUID())) {
            texture = new RawTextureResource(mutation.getCurrentTexture(), 18, 18, 0, 0);

            currentNode = mutation.getCurrentNode(Minecraft.getInstance().player.getUUID());

            legalOptionals = currentNode.getOptional()
                    .stream()
                    .map(MutationTree.Node::getParent)
                    .filter(parent -> parent.isLegalMutation(MutateHelper.getClientManager()))
                    .map(Mutation::getRegistryName)
                    .collect(Collectors.toList());
        }

        int highestX = 0;
        int highestY = 0;

        Map<Integer, Layer> childLayers = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            childLayers.computeIfAbsent(i, Layer::new);
        }

        for (MutationTree.Node node : tree) {
            int nodeX = node.getX();
            int nodeY = node.getY();

            if (nodeX > highestX) highestX = x;
            if (nodeY > highestY) highestY = y;

            nodeX = (nodeX * 18) + nodeX + 18 + 9;
            nodeY = (nodeY * 18) + nodeY + 9;

            List<MutationTree.Node> allNodes = node.getNext();
            allNodes.addAll(node.getOptional());

            for (MutationTree.Node next : allNodes) {
                int nextX = (next.getX() * 18) + nodeX + 18 + 9;
                int nextY = (next.getY() * 18) + nodeY + 9;

                int diffX = nextX - x;
                int diffY = nextY - y;



                if (diffX != 0) {
                    if (diffY > diffX) {
                        childLayers.get(0).addBuilder(WSElement.builder(nodeX + 5, nextY + 5, diffX, 4, this)
                                        .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_HORIZONTAL)));
                    } else {
                        childLayers.get(0).addBuilder(WSElement.builder(nodeX + 5, nodeY + 5, diffX, 4, this)
                                        .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_HORIZONTAL)));
                    }
                }

                if (diffY != 0) {
                    if (diffX > diffY) {
                        childLayers.get(0).addBuilder(WSElement.builder(nextX + 5, y + 5, 4, diffY, this)
                                        .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_VERTICAL)));
                    } else {
                        childLayers.get(0).addBuilder(WSElement.builder(x + 5, y + 5, 4, diffY, this)
                                        .addComponent(ImageComponent.factory(Textures.MUTATION_TREE_LINE_VERTICAL)));
                    }
                }
            }

            Builder currentBuilder = WSElement.builder(x, y, 18, 18, this)
                    .addComponent(ImageComponent.factory(texture));

            List<ITextComponent> conditionTooltips = node.getParent().getConditionTooltips();

            if (conditionTooltips.size() > 0)
                currentBuilder.addComponent(ToolTipComponent.factory(new TranslationTextComponent("generic.required.conditions").append(":")))
                    .addComponent(ToolTipComponent.factory(node.getParent().getConditionTooltips().toArray(new ITextComponent[0])));

            if (currentNode != null && legalOptionals.contains(node.getRegistryKey())) {
                currentBuilder.addComponent(ChooseOptionalComponent.factory(mutation, currentNode.getParent()));
            }

            childLayers.get(4).addBuilder(currentBuilder);
        }

        Builder treeBuilder = TileSheetElement.builder(x + 18, y, (highestX + 2) * 18, (highestY + 2) * 18, this, Textures.SHEET_CONTAINER);

        for (Layer layer : childLayers.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList())) {
            for (Builder builder : layer.getBuilders()) {
                treeBuilder.addChild(builder, layer.getId());
            }
        }

        mutationBuilder.addComponent(HoveredElementComponent.factory(treeBuilder));

        return mutationBuilder;
    }
}