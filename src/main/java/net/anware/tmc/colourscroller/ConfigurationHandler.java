package net.anware.tmc.colourscroller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import static net.anware.tmc.colourscroller.ScrollableHelper.SCROLLABLE_SETS;
import static net.anware.tmc.colourscroller.ScrollableHelper.addSet;
import static net.anware.tmc.colourscroller.ScrollableHelper.ColouredEntry;
import static net.anware.tmc.colourscroller.ScrollableHelper.rebuildIndexAndApplyToItems;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurationHandler {

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(ColourScroller.ID + "/scrollables.json");
    public static final Path FIRST_LAUNCH_PATH = FabricLoader.getInstance().getConfigDir().resolve(ColourScroller.ID + "/firstlaunch.json");
    public static final Path SETTINGS_PATH = FabricLoader.getInstance().getConfigDir().resolve(ColourScroller.ID + "/settings.json");

    public static boolean FIRST_LAUNCH = false;

    public static boolean SYNC_ENABLED = false;

    public static class ScrollerSettings {
        public boolean syncEnabled = true;
        public ScrollerSettings() {}
    }

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

        loadSettings();
    }

    public static void loadSettings() {
        if (Files.exists(SETTINGS_PATH)) {
            try {
                ScrollerSettings s = mapper.readValue(SETTINGS_PATH.toFile(), ScrollerSettings.class);
                if (s != null){
                    SYNC_ENABLED = s.syncEnabled;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveSettings();
        }
    }

    public static void saveSettings() {
        try {
            Files.createDirectories(SETTINGS_PATH.getParent());
            ScrollerSettings s = new ScrollerSettings();
            s.syncEnabled = SYNC_ENABLED;
            mapper.writeValue(SETTINGS_PATH.toFile(), s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadSetsFromJson() {
        try {
            List<ConfigSet> configSets = mapper.readValue(
                    CONFIG_PATH.toFile(),
                    new TypeReference<List<ConfigSet>>() {}
            );

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

                    String pathOnly = id.getPath();

                    entries.add(new ScrollableHelper.ColouredEntry(set.type, pathOnly, () -> item));
                }

                if (!entries.isEmpty()) {
                    ScrollableHelper.addSet(entries);
                }
            }

            ScrollableHelper.rebuildIndexAndApplyToItems();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSetsToJson() {
        List<ConfigSet> configSets = new ArrayList<>();
        for (List<ColouredEntry> set : SCROLLABLE_SETS) {
            if (set == null || set.isEmpty()) continue;
            String type = set.get(0).type();
            List<Item> items = new ArrayList<>();
            for (ColouredEntry entry : set) {
                items.add(entry.item().get());
            }
            configSets.add(new ConfigSet(type, items));
        }
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            mapper.writeValue(CONFIG_PATH.toFile(), configSets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkFirstLaunch() {
        if (!Files.exists(SETTINGS_PATH)) {
            FIRST_LAUNCH = true;
            return true;
        }
        FIRST_LAUNCH = false;
        return false;
    }
}
