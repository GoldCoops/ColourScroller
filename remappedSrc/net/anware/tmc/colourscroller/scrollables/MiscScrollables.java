package net.anware.tmc.colourscroller.scrollables;

import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class MiscScrollables {
    public static void init() {
        addSet("ice",
                Items.ICE,
                Items.PACKED_ICE,
                Items.BLUE_ICE
        );

        addSet("pumpkin",
                Items.PUMPKIN,
                Items.CARVED_PUMPKIN
        );

        addSet("bukkit",
                Items.BUCKET,
                Items.WATER_BUCKET,
                Items.LAVA_BUCKET,
                Items.POWDER_SNOW_BUCKET
        );

        addSet("storage",
                Items.CHEST,
                Items.BARREL,
                Items.DECORATED_POT,
                Items.CHISELED_BOOKSHELF,
                Items.ENDER_CHEST,
                Items.COMPOSTER,
                Items.CAULDRON,
                Items.LECTERN
        );

        addSet("lightitems",
                Items.TORCH,
                Items.SOUL_TORCH,
                Items.CAMPFIRE,
                Items.SOUL_CAMPFIRE,
                Items.LANTERN,
                Items.SOUL_LANTERN
        );

        addSet("lightblocks",
                Items.SEA_LANTERN,
                Items.GLOWSTONE,
                Items.SHROOMLIGHT,
                Items.OCHRE_FROGLIGHT,
                Items.PEARLESCENT_FROGLIGHT,
                Items.VERDANT_FROGLIGHT
        );

        addSet("itemframe",
                Items.ITEM_FRAME,
                Items.GLOW_ITEM_FRAME
        );


        addSet("heads",
                Items.SKELETON_SKULL,
                Items.WITHER_SKELETON_SKULL,
                Items.PLAYER_HEAD,
                Items.ZOMBIE_HEAD,
                Items.CREEPER_HEAD,
                Items.DRAGON_HEAD,
                Items.PIGLIN_HEAD
        );

        // 2) Infested blocks
        addSet("infestedblock",
                Items.INFESTED_STONE,
                Items.INFESTED_COBBLESTONE,
                Items.INFESTED_STONE_BRICKS,
                Items.INFESTED_MOSSY_STONE_BRICKS,
                Items.INFESTED_CRACKED_STONE_BRICKS,
                Items.INFESTED_CHISELED_STONE_BRICKS,
                Items.INFESTED_DEEPSLATE
        );

        // 3) Workstations
        addSet("workstation",
                Items.CRAFTING_TABLE,
                Items.FURNACE,
                Items.SMOKER,
                Items.BLAST_FURNACE,
                Items.LOOM,
                Items.CARTOGRAPHY_TABLE,
                Items.FLETCHING_TABLE
        );



    }
}
