package net.anware.tmc.colourscroller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static net.anware.tmc.colourscroller.ScrollableHelper.SCROLLABLE_SETS;
import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;
import static net.anware.tmc.colourscroller.ScrollableHelper.ColouredEntry;
import static net.anware.tmc.colourscroller.ScrollableHelper.rebuildIndexAndApplyToItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurationHandler {

    public static final Path CONFIG_PATH = Paths.get("ColourScroller/config", ColourScroller.ID + ".json");
    public static final Path FIRST_LAUNCH_PATH = Paths.get("ColourScroller/config", ColourScroller.ID + "_first_launch.json");

    public static boolean FIRST_LAUNCH = false;

    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);


    public static class ConfigSet {
        public String type;
        public List<String> items;
        public ConfigSet() {

        }

        public ConfigSet(String type, List<Item> items) {
            this.type = type;
            this.items = new ArrayList<>();
            for (Item item : items) {
                this.items.add(Registries.ITEM.getId(item).toString());
            }
        }
    }

    public static void init() {
        if (FIRST_LAUNCH) {
            List<ConfigSet> set = new ArrayList<>();
            for (int i = 0; i < SCROLLABLE_SETS.size(); i++) {
                if (SCROLLABLE_SETS.get(i) == null || SCROLLABLE_SETS.get(i).isEmpty()) continue;
                int size = SCROLLABLE_SETS.get(i).size();
                String type = SCROLLABLE_SETS.get(i).get(0).type();
                List<Item> items = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    items.add(SCROLLABLE_SETS.get(i).get(j).item().get());
                }
                set.add(new ConfigSet(type, items));

            }
            try {
                mapper.writeValue(CONFIG_PATH.toFile(), set);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createDirectories(CONFIG_PATH.getParent());
                Files.createFile(CONFIG_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void loadSetsFromJson() {
        try {
            List<ConfigSet> configSets = mapper.readValue(
                    CONFIG_PATH.toFile(),
                    new TypeReference<List<ConfigSet>>() {}
            );

            // Clear existing sets and rebuild from config
            ScrollableHelper.clearAllSets();

            for (ConfigSet set : configSets) {
                if (set == null || set.type == null || set.items == null || set.items.isEmpty()) continue;

                ArrayList<ColouredEntry> entries = new ArrayList<>();

                for (String idString : set.items) {
                    if (idString == null || idString.isBlank()) continue;

                    Identifier id = Identifier.tryParse(idString);
                    if (id == null) {
                        System.err.println("[ColourScroller] Invalid item id in config: " + idString);
                        continue;
                    }

                    Item item = Registries.ITEM.get(id);
                    if (item == null) {
                        System.err.println("[ColourScroller] Unknown item id in config: " + idString);
                        continue;
                    }

                    // NOTE: your existing code keys lookup by path only. To keep compatibility,
                    // we store only the path here. (Better long-term: store full id string.)
                    String pathOnly = id.getPath();

                    entries.add(new ScrollableHelper.ColouredEntry(set.type, pathOnly, () -> item));
                }

                if (!entries.isEmpty()) {
                    ScrollableHelper.addSet(entries);
                }
            }

            // Rebuild lookup and apply metadata onto items
            ScrollableHelper.rebuildIndexAndApplyToItems();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkFirstLaunch() {
        if (!Files.exists(FIRST_LAUNCH_PATH)) {
            try {
                Files.createDirectories(FIRST_LAUNCH_PATH.getParent());
                Files.createFile(FIRST_LAUNCH_PATH);
                FIRST_LAUNCH = true;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FIRST_LAUNCH = false;
        return false;
    }
}
