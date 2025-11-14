package net.anware.tmc.colourscroller.scrollables;

import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class RedstoneScrollables {
    public static void init() {
        addSet("piston", Items.PISTON, Items.STICKY_PISTON);

        addSet("slimehoney", Items.HONEY_BLOCK, Items.SLIME_BLOCK);

        addSet("dispenserdropper", Items.DISPENSER, Items.DROPPER);

        addSet("rails", Items.RAIL, Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Items.POWERED_RAIL);

        addSet("input", Items.RAIL, Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Items.POWERED_RAIL);
    }
}
