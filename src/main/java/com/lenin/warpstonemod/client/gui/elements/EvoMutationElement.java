package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.Textures;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.components.ButtonComponent;
import com.lenin.warpstonemod.client.gui.components.ImageComponent;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.Mutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.MutationTree;
import com.lenin.warpstonemod.common.network.ChooseOptionalPacket;
import com.lenin.warpstonemod.common.network.PacketHandler;
import com.lenin.warpstonemod.common.network.SyncPlayerDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EvoMutationElement extends WSElement {

    protected EvolvingMutation mutation;

    public EvoMutationElement () {}

    public static class Builder extends WSElement.AbstractBuilder<EvoMutationElement> {

        protected Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen, EvolvingMutation _mutation) {
            super(_x, _y, _width, _height, _parentScreen, new EvoMutationElement());

            List<MutationTree.Node> tree = element.mutation.getChildNodes();
            List<WSElement.AbstractBuilder<? extends WSElement>> _childBuilders = new ArrayList<>();
            RawTextureResource texture = new RawTextureResource(_mutation.getTexture(), 18, 18, 0, 0);
            MutationTree.Node currentNode;
            List<ResourceLocation> legalOptionals = new ArrayList<>();

            if (element.mutation.containsInstance(Minecraft.getInstance().player.getUniqueID())) {
                texture = new RawTextureResource(_mutation.getCurrentTexture(), 18, 18, 0, 0);

                currentNode = _mutation.getCurrentNode(Minecraft.getInstance().player.getUniqueID());

                legalOptionals = currentNode.getOptional()
                        .stream()
                        .map(MutationTree.Node::getParent)
                        .filter(parent -> parent.isLegalMutation(MutateHelper.getClientManager()))
                        .map(Mutation::getRegistryName)
                        .collect(Collectors.toList());
            }

            int highestX = 0;
            int highestY = 0;

            for (MutationTree.Node node : tree) {
                int x = node.getX();
                int y = node.getY();

                if (x > highestX) highestX = x;
                if (y > highestY) highestY = y;

                x = (x * 18) + element.x + 18 + 9;
                y = (y * 18) + element.y + 9;

                List<MutationTree.Node> allNodes = node.getNext();
                allNodes.addAll(node.getOptional());

                for (MutationTree.Node next : allNodes) {
                    int nextX = (next.getX() * 18) + element.x + 18 + 9;
                    int nextY = (next.getY() * 18) + element.y + 9;

                    int diffX = nextX - x;
                    int diffY = nextY - y;

                    if (diffX != 0) {
                        if (diffY > diffX) {
                            _childBuilders.add(new WSElement.Builder(x + 5, nextY + 5, diffX, 4, _parentScreen)
                                    .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_HORIZONTAL))
                            );
                        } else {
                            _childBuilders.add(new WSElement.Builder(x + 5, y + 5, diffX, 4, _parentScreen)
                                    .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_HORIZONTAL))
                            );
                        }
                    }

                    if (diffY != 0) {
                        if (diffX > diffY) {
                            _childBuilders.add(new WSElement.Builder(nextX + 5, y + 5, 4, diffY, _parentScreen)
                                    .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_VERTICAL))
                            );
                        } else {
                            _childBuilders.add(new WSElement.Builder(x + 5, y + 5, 4, diffY, _parentScreen)
                                    .addComponent(new ImageComponent(Textures.MUTATION_TREE_LINE_VERTICAL))
                            );
                        }
                    }
                }

                AbstractBuilder<MutationElement> currentBuilder = new MutationElement.Builder(x, y, 18, 18, _parentScreen, node.getParent());
                List<ITextComponent> conditionTooltips = node.getParent().getConditionTooltips();

                if (conditionTooltips.size() > 0)
                    currentBuilder.addTooltips(new TranslationTextComponent("generic.required.conditions").appendString(":"))
                            .addTooltips(node.getParent().getConditionTooltips().toArray(new ITextComponent[0]));

                if (legalOptionals.contains(node.getRegistryKey())) {
                    currentBuilder.addComponent(new ButtonComponent((ButtonComponent button) -> {
                        SyncPlayerDataPacket pkt = new ChooseOptionalPacket(Minecraft.getInstance().player.getUniqueID(), currentNode.getParent(), node.getParent());
                        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
                        PacketHandler.CHANNEL.sendToPlayer(server.getPlayerList().getPlayerByUUID(playerUUID), pkt);
                    }));
                }

                _childBuilders.add(currentBuilder);
            }

            addChild(new TileSheetElement.Builder(element.x + 18, element.y, (highestX + 2) * 18, (highestY + 2) * 18, _parentScreen, Textures.SHEET_CONTAINER));
            element.childBuilders.addAll(_childBuilders);

            addComponent(new ImageComponent(texture));
        }
    }
}