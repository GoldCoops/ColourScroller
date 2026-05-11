package net.anware.tmc.colourscroller;


import net.anware.tmc.colourscroller.scrollables.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import static net.anware.tmc.colourscroller.ConfigurationHandler.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        checkFirstLaunch();

        if (!FIRST_LAUNCH) loadSetsFromJson(); else {
            ColourScrollables.init();
            OtherBlockScrollables.init();
            RedstoneScrollables.init();
            MiscScrollables.init();
            WoodScrollables.init();
            BuildingBlockScrollables.init();
            CopperBlockScrollables.init();
            NaturalBlockScroller.init();
            MobScrollables.init();
        }

        int removed = deduplicate();
        if (removed > 0) {
            System.out.println("[ColourScroller] Removed " + removed
                    + " duplicate item(s) on startup (an item may only belong to one set).");
            if (!FIRST_LAUNCH) {
                ConfigurationHandler.saveSetsToJson();
            }
        }

        rebuildIndexAndApplyToItems();

        System.out.println(SCROLLABLE_SETS);
    }

    public static int deduplicate() {
        Set<String> seen = new HashSet<>();
        int removed = 0;
        for (List<ColouredEntry> set : SCROLLABLE_SETS) {
            if (set == null) continue;
            Iterator<ColouredEntry> it = set.iterator();
            while (it.hasNext()) {
                ColouredEntry entry = it.next();
                if (!seen.add(entry.id())) {
                    System.out.println("[ColourScroller] Duplicate item '" + entry.id()
                            + "' (type=" + entry.type() + ") — removing later occurrence.");
                    it.remove();
                    removed++;
                }
            }
        }
        SCROLLABLE_SETS.removeIf(s -> s == null || s.isEmpty());
        return removed;
    }


    public static void clearAllSets() {
        SCROLLABLE_SETS.clear();
        SCROLLABLE_LOOKUP.clear();
    }


    public static void rebuildIndexAndApplyToItems() {
        SCROLLABLE_LOOKUP.clear();

        for (Item item : Registries.ITEM) {
            if (item instanceof ScrollableItem scrollable) {
                scrollable.setScrollable(false);
                scrollable.setListIndex(0);
                scrollable.setIndex(0);
                scrollable.setType("");
            }
        }

        for (int listIndex = 0; listIndex < SCROLLABLE_SETS.size(); listIndex++) {
            List<ScrollableHelper.ColouredEntry> list = SCROLLABLE_SETS.get(listIndex);
            for (int index = 0; index < list.size(); index++) {
                SCROLLABLE_LOOKUP.put(
                        list.get(index).id(),
                        new ScrollableHelper.ScrollInfo(list.get(index).type(), listIndex, index)
                );
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

    public static void addSet(String type, Item... items) {
        List<ColouredEntry> set = new ArrayList<>();

        for (Item block : items) {
            String id = Registries.ITEM.getId(block).getPath();
            set.add(new ColouredEntry(type, id, block::asItem));
        }

        SCROLLABLE_SETS.add(set);
    }

    public static void addSet(ArrayList<ColouredEntry> set) {
        SCROLLABLE_SETS.add(set);
    }

    public static ColouredEntry entryFor(String type, Item item) {
        String id = Registries.ITEM.getId(item).getPath();
        return new ColouredEntry(type, id, () -> item);
    }

    public static void replaceAllSets(List<List<ColouredEntry>> newSets) {
        SCROLLABLE_SETS.clear();
        Set<String> seen = new HashSet<>();
        for (List<ColouredEntry> set : newSets) {
            if (set == null || set.isEmpty()) continue;
            List<ColouredEntry> copy = new ArrayList<>(set.size());
            for (ColouredEntry e : set) {
                if (seen.add(e.id())) copy.add(e);
            }
            if (!copy.isEmpty()) SCROLLABLE_SETS.add(copy);
        }
        rebuildIndexAndApplyToItems();
    }
}
