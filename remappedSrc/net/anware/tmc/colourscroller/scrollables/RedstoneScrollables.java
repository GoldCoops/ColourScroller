package net.anware.tmc.colourscroller.scrollables;

import net.minecraft.item.Items;

import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;

public class RedstoneScrollables {
    public static void init() {
        addSet("piston",
                Items.PISTON,
                Items.STICKY_PISTON
        );

        addSet("slimehoney",
                Items.HONEY_BLOCK,
                Items.SLIME_BLOCK
        );

        addSet("otherredstone",
                Items.DISPENSER,
                Items.DROPPER,
                Items.CRAFTER,
                Items.TARGET,
                Items.COMMAND_BLOCK
        );

        addSet("rails",
                Items.RAIL,
                Items.ACTIVATOR_RAIL,
                Items.DETECTOR_RAIL,
                Items.POWERED_RAIL
        );

        addSet("lamps",
                Items.REDSTONE_LAMP,
                Items.COPPER_BULB,
                Items.WAXED_COPPER_BULB,
                Items.OXIDIZED_COPPER_BULB,

                Items.WAXED_OXIDIZED_COPPER_BULB,
                Items.WEATHERED_COPPER_BULB,
                Items.WAXED_WEATHERED_COPPER_BULB,
                Items.EXPOSED_COPPER_BULB,

                Items.WAXED_EXPOSED_COPPER_BULB
        );

        addSet("input",
                Items.STRING,
                Items.TRIPWIRE_HOOK,
                Items.STONE_BUTTON,
                Items.POLISHED_BLACKSTONE_BUTTON,
                Items.LEVER,

                Items.STONE_PRESSURE_PLATE,
                Items.POLISHED_BLACKSTONE_PRESSURE_PLATE,
                Items.HEAVY_WEIGHTED_PRESSURE_PLATE,

                Items.LIGHT_WEIGHTED_PRESSURE_PLATE
        );

        addSet("sculk",
                Items.SCULK_SENSOR,
                Items.CALIBRATED_SCULK_SENSOR,
                Items.SCULK_SHRIEKER,
                Items.SCULK_CATALYST
        );

        addSet("transport",
                Items.MINECART,
                Items.HOPPER_MINECART,
                Items.CHEST_MINECART,
                Items.FURNACE_MINECART,
                Items.TNT_MINECART,

                Items.DARK_OAK_BOAT,
                Items.DARK_OAK_CHEST_BOAT,
                Items.BAMBOO_RAFT
        );
    }
}
