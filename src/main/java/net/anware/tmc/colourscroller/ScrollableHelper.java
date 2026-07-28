package net.anware.tmc.colourscroller;


import net.anware.tmc.colourscroller.scrollables.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import static net.anware.tmc.colourscroller.ConfigurationHandler.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ScrollableHelper {

    private static final List<List<ColouredEntry>> SCROLLABLE_SETS = new ArrayList<>();
    public record ColouredEntry(String type, String id, Supplier<Item> item) {
        //no-op
    }
    private static final Map<String, ScrollInfo> SCROLLABLE_LOOKUP = new HashMap<>();
    private record ScrollInfo(String type, int listIndex, int index) {
        //no-op
    }

    /**
     * Read-only view of every scrollable set, in list order. Both the returned list
     * and each set inside it are immutable - mutate through {@link #addSet},
     * {@link #replaceAllSets}, {@link #clearAllSets} or {@link #reloadDefaultSets}
     * so the lookup index is rebuilt alongside the sets.
     */
    public static List<List<ColouredEntry>> getSets() {
        return Collections.unmodifiableList(SCROLLABLE_SETS);
    }

    /** Number of scrollable sets currently loaded. */
    public static int setCount() {
        return SCROLLABLE_SETS.size();
    }

    /**
     * The set at {@code listIndex}, or an empty list when the index is out of range.
     * Items cache their list index, so a stale index left over from a previous set
     * layout yields an empty set rather than an exception.
     */
    public static List<ColouredEntry> getSet(int listIndex) {
        if (listIndex < 0 || listIndex >= SCROLLABLE_SETS.size()) return List.of();
        return SCROLLABLE_SETS.get(listIndex);
    }

    public static void initialize() {
        checkFirstLaunch();

        if (!FIRST_LAUNCH) loadSetsFromJson(); else {
            initDefaultSets();
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


    public static void reloadDefaultSets() {
        clearAllSets();
        initDefaultSets();
        deduplicate();
        rebuildIndexAndApplyToItems();
        ConfigurationHandler.saveSetsToJson();
    }


    public static void initDefaultSets() {
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

    public static int deduplicate() {
        Set<String> seen = new HashSet<>();
        int removed = 0;
        List<List<ColouredEntry>> rebuilt = new ArrayList<>(SCROLLABLE_SETS.size());

        for (List<ColouredEntry> set : SCROLLABLE_SETS) {
            if (set == null || set.isEmpty()) continue;
            List<ColouredEntry> kept = new ArrayList<>(set.size());
            for (ColouredEntry entry : set) {
                if (seen.add(entry.id())) {
                    kept.add(entry);
                } else {
                    System.out.println("[ColourScroller] Duplicate item '" + entry.id()
                            + "' (type=" + entry.type() + ") — removing later occurrence.");
                    removed++;
                }
            }
            if (!kept.isEmpty()) rebuilt.add(List.copyOf(kept));
        }

        SCROLLABLE_SETS.clear();
        SCROLLABLE_SETS.addAll(rebuilt);
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

        SCROLLABLE_SETS.add(List.copyOf(set));
    }

    public static void addSet(List<ColouredEntry> set) {
        SCROLLABLE_SETS.add(List.copyOf(set));
    }

    public static ColouredEntry entryFor(String type, Item item) {
        String id = Registries.ITEM.getId(item).getPath();
        return new ColouredEntry(type, id, () -> item);
    }

    public static void replaceAllSets(List<List<ColouredEntry>> newSets) {
        SCROLLABLE_SETS.clear();
        for (List<ColouredEntry> set : newSets) {
            if (set == null || set.isEmpty()) continue;
            SCROLLABLE_SETS.add(List.copyOf(set));
        }
        deduplicate();
        rebuildIndexAndApplyToItems();
    }
}
