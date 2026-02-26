package net.anware.tmc.colourscroller.scrollables;

import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class NaturalBlockScroller {
    public static void init() {
        addSet("coral",
                Items.TUBE_CORAL,
                Items.TUBE_CORAL_FAN,
                Items.DEAD_TUBE_CORAL,
                Items.DEAD_TUBE_CORAL_FAN,
                Items.BRAIN_CORAL,
                Items.BRAIN_CORAL_FAN,
                Items.DEAD_BRAIN_CORAL,
                Items.DEAD_BRAIN_CORAL_FAN,
                Items.BUBBLE_CORAL,
                Items.BUBBLE_CORAL_FAN,
                Items.DEAD_BUBBLE_CORAL,
                Items.DEAD_BUBBLE_CORAL_FAN,
                Items.FIRE_CORAL,
                Items.FIRE_CORAL_FAN,
                Items.DEAD_FIRE_CORAL,
                Items.DEAD_FIRE_CORAL_FAN,
                Items.HORN_CORAL,
                Items.HORN_CORAL_FAN,
                Items.DEAD_HORN_CORAL,
                Items.DEAD_HORN_CORAL_FAN
        );

        addSet("coralBlock",
                Items.TUBE_CORAL_BLOCK,
                Items.BRAIN_CORAL_BLOCK,
                Items.BUBBLE_CORAL_BLOCK,
                Items.FIRE_CORAL_BLOCK,
                Items.HORN_CORAL_BLOCK,
                Items.DEAD_TUBE_CORAL_BLOCK,
                Items.DEAD_BRAIN_CORAL_BLOCK,
                Items.DEAD_BUBBLE_CORAL_BLOCK,
                Items.DEAD_FIRE_CORAL_BLOCK,
                Items.DEAD_HORN_CORAL_BLOCK
        );

        addSet("flower",
                Items.DANDELION,
                Items.POPPY,
                Items.BLUE_ORCHID,
                Items.ALLIUM,
                Items.AZURE_BLUET,
                Items.RED_TULIP,
                Items.ORANGE_TULIP,
                Items.WHITE_TULIP,
                Items.PINK_TULIP,
                Items.OXEYE_DAISY,
                Items.CORNFLOWER,
                Items.LILY_OF_THE_VALLEY,
                Items.WITHER_ROSE,
                Items.TORCHFLOWER,
                Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_POD,
                Items.PITCHER_PLANT,
                Items.SPORE_BLOSSOM,
                Items.BROWN_MUSHROOM,
                Items.RED_MUSHROOM,
                Items.OPEN_EYEBLOSSOM,
                Items.CLOSED_EYEBLOSSOM
        );

        addSet("grassesandvines",
                Items.SHORT_GRASS,
                Items.TALL_GRASS,
                Items.LARGE_FERN,
                Items.FERN,
                Items.DEAD_BUSH,
                Items.SEAGRASS,
                Items.KELP,
                Items.VINE,
                Items.LEAF_LITTER,
                Items.MOSS_CARPET,
                Items.MOSS_BLOCK,
                Items.PALE_MOSS_CARPET,
                Items.PALE_MOSS_BLOCK,
                Items.PALE_HANGING_MOSS,
                Items.HANGING_ROOTS,
                Items.BIG_DRIPLEAF,
                Items.SMALL_DRIPLEAF,
                Items.SUGAR_CANE,
                Items.WILDFLOWERS,
                Items.PINK_PETALS
        );

    }
}
