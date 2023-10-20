package com.lenin.warpstonemod.client.gui.elements;

import com.lenin.warpstonemod.client.gui.RawTextureResource;
import com.lenin.warpstonemod.client.gui.SpriteSheetResource;
import com.lenin.warpstonemod.client.gui.WSElement;
import com.lenin.warpstonemod.client.gui.screens.WSScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class TileSheetElement extends WSElement {
    //prototype implementation

    protected final SpriteSheetResource spriteSheet;

    protected final List<RawTextureResource> textures = new ArrayList<>();

    public static WSElement.Builder builder (int _x, int _y, int _width, int _height, WSScreen _parentScreen, SpriteSheetResource _spriteSheet) {
        return new Builder(_x, _y, _width, _height, _parentScreen, _spriteSheet);
    }

    protected TileSheetElement(Builder builder) {
        super(builder);
        spriteSheet = builder.spriteSheet;

        for (int i = 0; i < 9; i++) {
            textures.add(spriteSheet.resolve(i));
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //super.render(matrixStack, mouseX, mouseY, partialTicks);

        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(0);

        for (int i = 0; i < 9; i++) {
            RawTextureResource activeTexture = textures.get(i);
            minecraft.getTextureManager().bind(activeTexture.resource);

            //int tileY = y + (int) (i / 3f);
            //int tileX = x + (i - (tileY * 3));

            int tileX = x;
            int tileY = y;
            int tileWidth = 18;
            int tileHeight = 18;

            switch (i) {
                case 1:
                    tileX = x + 18;
                    tileWidth = width - (18 * 2);
                    break;
                case 2:
                    tileX = x + (width - 18);
                    break;
                case 3:
                    tileY = y + 18;
                    tileHeight = height - (18 * 2);
                    break;
                case 4:
                    tileX = x + 18;
                    tileY = y + 18;
                    tileWidth = width - (18 * 2);
                    tileHeight = height - (18 * 2);
                    break;
                case 5:
                    tileX = x + (width - 18);
                    tileY = y + 18;
                    tileHeight = height - (18 * 2);
                    break;
                case 6:
                    tileY = y + (height - 18);
                    break;
                case 7:
                    tileX = x + 18;
                    tileY = y + (height - 18);
                    tileWidth = width - (18 * 2);
                    break;
                case 8:
                    tileX = x + (width - 18);
                    tileY = y + (height - 18);
                    break;
            }

            blit(matrixStack, tileX, tileY,
                    activeTexture.posX, activeTexture.posY,
                    tileWidth, tileHeight,
                    activeTexture.sourceSizeX, activeTexture.sourceSizeY
            );
        }
    }

    public static class Builder extends WSElement.Builder {

        SpriteSheetResource spriteSheet;

        public Builder(int _x, int _y, int _width, int _height, WSScreen _parentScreen, SpriteSheetResource _spriteSheet) {
            super(_x, _y, _width, _height, _parentScreen);

            spriteSheet = _spriteSheet;
        }

        @Override
        public WSElement build() {
            return new TileSheetElement(this);
        }
    }
}