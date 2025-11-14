package net.anware.tmc.colourscroller;


import net.anware.tmc.colourscroller.scrollables.ColourScrollables;
import net.anware.tmc.colourscroller.scrollables.DirtScrollables;
import net.anware.tmc.colourscroller.scrollables.OreScrollables;
import net.anware.tmc.colourscroller.scrollables.RedstoneScrollables;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ScrollableHelper {

    public static final List<List<ColouredEntry>> SCROLLABLE_SETS = new ArrayList<>();
    public record ColouredEntry(String type, String id, Supplier<Item> item) {
        //no-op
    }
    private static final Map<String, ScrollInfo> SCROLLABLE_LOOKUP = new HashMap<>();
    private record ScrollInfo(String type, int listIndex, int index) {
        //no-op
    }

    public static void initialize() {
        ColourScrollables.init();
        DirtScrollables.init();
        OreScrollables.init();
        RedstoneScrollables.init();

        for (int listIndex = 0; listIndex < SCROLLABLE_SETS.size(); listIndex++) {
            List<ScrollableHelper.ColouredEntry> list = SCROLLABLE_SETS.get(listIndex);
            for (int index = 0; index < list.size(); index++) {
                SCROLLABLE_LOOKUP.put(list.get(index).id(), new ScrollableHelper.ScrollInfo(list.get(index).type(), listIndex, index));
            }
        }

        for (Item item : Registries.ITEM) {
            String idPath = Registries.ITEM.getId(item).getPath();
            ScrollableHelper.ScrollInfo info = ScrollableHelper.SCROLLABLE_LOOKUP.get(idPath);

            if (info != null && item instanceof ScrollableItem scrollable) {
                scrollable.setScrollable(true);
                scrollable.setListIndex(info.listIndex());
                scrollable.setIndex(info.index());
                scrollable.setType(info.type());
            }
        }
    }

    public static void addSet(String type, Item... blocks) {
        List<ColouredEntry> set = new ArrayList<>();

        for (Item block : blocks) {
            String id = Registries.ITEM.getId(block).getPath();
            set.add(new ColouredEntry(type, id, block::asItem));
        }

        SCROLLABLE_SETS.add(set);
    }
}
