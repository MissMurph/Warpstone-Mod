package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.util.ResourceLocation;

public class Textures {
    public static void register (){}

        /*  Raw Textures    */
    public static final RawTextureResource MUT_OPEN_SCREEN_BUTTON = new RawTextureResource(
            new ResourceLocation(WarpstoneMain.MOD_ID,"textures/gui/warp_icons.png"), 20, 37, 0, 0);

    public static final RawTextureResource MUT_OPEN_SCREEN_BUTTON_HVRD = new RawTextureResource(
            new ResourceLocation(WarpstoneMain.MOD_ID,"textures/gui/warp_icons.png"), 20, 37, 0, 19);

    public static final RawTextureResource MUT_SCREEN_ATTR_BAR = new RawTextureResource(
            new ResourceLocation(WarpstoneMain.MOD_ID,"textures/gui/mutation_attribute_bar.png"), 256, 256, 0, 19);

    public static final RawTextureResource MUT_SCREEN = new RawTextureResource(
            new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/mutation_screen.png"), 256, 256, 0, 0);

    public static final RawTextureResource CORRUPTED_TOME_SCREEN = new RawTextureResource(
            new ResourceLocation(WarpstoneMain.MOD_ID, "textures/gui/corrupted_tome.png"), 256, 256, 0, 0);

        /*  Sprite Sheets   */
    public static final SpriteSheetResource SHEET_MUT_ATTR_BAR = new SpriteSheetResource(MUT_SCREEN_ATTR_BAR, 75,
            (int frame) -> {
                int x;
                int y;

                if (frame < 25)  {
                    x = frame;
                    y = 0;
                }
                else if (frame < 57) {
                    x = frame - 25;
                    y = 1;
                }
                else {
                    x = frame - 57;
                    y = 2;
                }

                return new RawTextureResource(new ResourceLocation(WarpstoneMain.MOD_ID,"textures/gui/mutation_attribute_bar.png"), 256, 256, x*8, y*78);
            }
    );
}