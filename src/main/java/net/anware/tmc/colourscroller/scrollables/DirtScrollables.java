package net.anware.tmc.colourscroller.scrollables;


import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class DirtScrollables {
    public static void init() {
        addSet("dirt", Items.DIRT, Items.COARSE_DIRT, Items.ROOTED_DIRT, Items.DIRT_PATH, Items.FARMLAND,
                Items.MYCELIUM, Items.WARPED_NYLIUM, Items.CRIMSON_NYLIUM, Items.NETHERRACK, Items.END_STONE);
    }
}
