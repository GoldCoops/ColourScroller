package net.anware.tmc.colourscroller.scrollables;


import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class OtherBlockScrollables {
    public static void init() {
        addSet("dirt",
                Items.DIRT,
                Items.COARSE_DIRT,
                Items.ROOTED_DIRT,
                Items.DIRT_PATH,
                Items.FARMLAND,
                Items.MYCELIUM,
                Items.WARPED_NYLIUM,
                Items.CRIMSON_NYLIUM,
                Items.NETHERRACK,
                Items.END_STONE,
                Items.MUD,
                Items.MOSS_BLOCK,
                Items.PALE_MOSS_BLOCK,
                Items.SOUL_SAND,
                Items.SOUL_SOIL
        );

        addSet("ore",
                Items.IRON_ORE,
                Items.GOLD_ORE,
                Items.COPPER_ORE,
                Items.COAL_ORE,
                Items.DIAMOND_ORE,
                Items.EMERALD_ORE,
                Items.LAPIS_ORE,
                Items.REDSTONE_ORE,
                Items.NETHER_GOLD_ORE,
                Items.NETHER_QUARTZ_ORE
        );

        addSet("ore",
                Items.DEEPSLATE_IRON_ORE,
                Items.DEEPSLATE_GOLD_ORE,
                Items.DEEPSLATE_COPPER_ORE,
                Items.DEEPSLATE_COAL_ORE,
                Items.DEEPSLATE_DIAMOND_ORE,
                Items.DEEPSLATE_EMERALD_ORE,
                Items.DEEPSLATE_LAPIS_ORE,
                Items.DEEPSLATE_REDSTONE_ORE,
                Items.GILDED_BLACKSTONE,
                Items.OBSIDIAN
        );
    }
}
