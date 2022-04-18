package com.lenin.warpstonemod.client.gui;

import com.lenin.warpstonemod.common.Warpstone;
import net.minecraft.util.ResourceLocation;

public class Textures {
    public static void register (){}

        /*  Raw Textures    */
    public static final RawTextureResource MUT_OPEN_SCREEN_BUTTON = new RawTextureResource(
            Warpstone.key("textures/gui/warp_icons.png"), 20, 37, 0, 0);

    public static final RawTextureResource MUT_OPEN_SCREEN_BUTTON_HVRD = new RawTextureResource(
            Warpstone.key("textures/gui/warp_icons.png"), 20, 37, 0, 19);

    public static final RawTextureResource MUT_SCREEN_ATTR_BAR = new RawTextureResource(
            Warpstone.key("textures/gui/mutation_attribute_bar.png"), 256, 256, 0, 19);

    public static final RawTextureResource MUT_SCREEN = new RawTextureResource(
            Warpstone.key("textures/gui/mutation_screen.png"), 256, 256, 0, 0);

    public static final RawTextureResource CORRUPTED_TOME_SCREEN = new RawTextureResource(
            Warpstone.key("textures/gui/corrupted_tome.png"), 256, 256, 0, 0);

    public static final RawTextureResource MUTATION_TREE_LINE_HORIZONTAL = new RawTextureResource(
            Warpstone.key("textures/gui/mutation_tree_line_horizontal.png"), 1, 8, 0, 0);

    public static final RawTextureResource MUTATION_TREE_LINE_VERTICAL = new RawTextureResource(
            Warpstone.key("textures/gui/mutation_tree_line_vertical.png"), 8, 1, 0, 0);

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

                return new RawTextureResource(Warpstone.key("textures/gui/mutation_attribute_bar.png"), 256, 256, x*8, y*78);
            }
    );

    public static final SpriteSheetResource SHEET_CONTAINER = new SpriteSheetResource(null, 9, (int frame) -> {
           // int y = (int) (frame / 3f);
            //int x = frame - (y * 3);

            //return new RawTextureResource(Warpstone.key("textures/gui/container_sheet.png"), 18, 18, x * 18, y * 18);

            switch (frame) {
                case 0:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_corner_top_left.png"), 18, 18, 0, 0);
                case 1:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_edge_top.png"), 18, 18, 0, 0);
                case 2:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_corner_top_right.png"), 18, 18, 0, 0);
                case 3:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_edge_left.png"), 18, 18, 0, 0);
                case 4:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_center.png"), 18, 18, 0, 0);
                case 5:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_edge_right.png"), 18, 18, 0, 0);
                case 6:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_corner_bottom_left.png"), 18, 18, 0, 0);
                case 7:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_edge_bottom.png"), 18, 18, 0, 0);
                case 8:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_corner_bottom_right.png"), 18, 18, 0, 0);
                default:
                    return new RawTextureResource(Warpstone.key("textures/gui/container_sheet.png"), 18, 18, 0, 0);
            }
        }
    );
}