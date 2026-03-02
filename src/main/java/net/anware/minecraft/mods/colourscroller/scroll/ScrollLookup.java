package net.anware.minecraft.mods.colourscroller.scroll;

import net.anware.minecraft.mods.colourscroller.Client;
import net.anware.minecraft.mods.colourscroller.util.DataFile;
import net.anware.minecraft.mods.colourscroller.util.GameUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollLookup {
	
	public static final Map<String, Scroll> SCROLL_REGISTRY = new HashMap<>();
	public static final Map<Item, Scroll> SCROLL_LOOKUP = new HashMap<>();
	public static final Path SCROLL_FOLDER = DataFile.CONFIG_PATH.resolve("scrolls");
	
	/* -------------------- Files ------------------------- */
	
	public static void load() {
		try {
			Files.createDirectories(SCROLL_FOLDER);
			try (var stream = Files.list(SCROLL_FOLDER).filter(p -> p.toString().endsWith(".json"))) {
				List<Path> files = stream.toList();
				for (Path path : files) {
					try {
						DataFile file = new DataFile(path);
						if (!file.root.has("scroll")) {
							continue;
						}
						if (!file.root.get("scroll").isJsonObject()) {
							continue;
						}
						var e0 = file.root.getAsJsonObject("scroll");
						String id = e0.get("id").getAsString();
						List<Item> items = new ArrayList<>();
						if (e0.has("items")) {
							for (var e1 : e0.getAsJsonArray("items")) {
								Item item = GameUtil.getItem(e1.getAsString());
								if (item != null) items.add(item);
							}
						}
						register(new Scroll(id, items));
					} catch (Exception ex) {
						Client.printError("failed to load scroll from " + path + ": " + ex);
					}
				}
				if (files.isEmpty()) {
					loadDefaultScrolls();
				}
			}
		} catch (Exception e) {
			Client.printError("failed to load scrolls: " + e);
		}
	}
	
	public static void save() {
		for (Scroll scroll : SCROLL_REGISTRY.values()) {
			scroll.save();
		}
	}
	
	/* -------------------- Scrolls ------------------------- */
	
	public static void register(Scroll scroll) {
		if (SCROLL_REGISTRY.containsKey(scroll.id)) {
			return;
		}
		for (Item item : scroll.items) if (SCROLL_LOOKUP.containsKey(item)) {
			return;
		}
		for (Item item : scroll.items) {
			SCROLL_LOOKUP.put(item, scroll);
		}
		SCROLL_REGISTRY.put(scroll.id, scroll);
	}
	
	public static void deleteScroll(Scroll scroll) {
		for (Item item : scroll.items) {
			SCROLL_LOOKUP.remove(item);
		}
		SCROLL_REGISTRY.remove(scroll.id);
		try {
			Files.deleteIfExists(SCROLL_FOLDER.resolve(scroll.id + ".json"));
		} catch (IOException e) {
			Client.printError("failed to remove json file for " + scroll.id);
		}
	}
	
	public static void changeScrollID(Scroll scroll, String id) {
		deleteScroll(scroll);
		scroll.id = id;
		register(scroll);
		scroll.save();
	}
	
	public static Scroll findScroll(String id) {
		return SCROLL_REGISTRY.get(id);
	}
	
	public static Scroll findScroll(Item item) {
		return SCROLL_LOOKUP.get(item);
	}
	
	/* -------------------- Items ------------------------- */
	
	public static Item getShifted(Item item, int shift) {
		Scroll scroll =  SCROLL_LOOKUP.get(item);
		if (scroll == null) {
			return null;
		}
		return scroll.getShifted(item, shift);
	}
	
	public static void addItem(Scroll scroll, Item item) {
		if (SCROLL_LOOKUP.containsKey(item)) {
			return;
		}
		SCROLL_LOOKUP.put(item, scroll);
		scroll.addItem(item);
	}
	
	public static void deleteItem(Item item) {
		SCROLL_LOOKUP.remove(item);
	}
	
	/* -------------------- Default Lists ------------------------- */
	
	protected static void loadDefaultScrolls() {
		register(new Scroll("wool", new ArrayList<>(){{
			add(Items.WHITE_WOOL);
			add(Items.LIGHT_GRAY_WOOL);
			add(Items.GRAY_WOOL);
			add(Items.BLACK_WOOL);
			add(Items.RED_WOOL);
			add(Items.BROWN_WOOL);
			add(Items.ORANGE_WOOL);
			add(Items.YELLOW_WOOL);
			add(Items.LIME_WOOL);
			add(Items.GREEN_WOOL);
			add(Items.CYAN_WOOL);
			add(Items.LIGHT_BLUE_WOOL);
			add(Items.BLUE_WOOL);
			add(Items.PINK_WOOL);
			add(Items.MAGENTA_WOOL);
			add(Items.PURPLE_WOOL);
		}}));
		register(new Scroll("concrete", new ArrayList<>(){{
			add(Items.WHITE_CONCRETE);
			add(Items.LIGHT_GRAY_CONCRETE);
			add(Items.GRAY_CONCRETE);
			add(Items.BLACK_CONCRETE);
			add(Items.RED_CONCRETE);
			add(Items.BROWN_CONCRETE);
			add(Items.ORANGE_CONCRETE);
			add(Items.YELLOW_CONCRETE);
			add(Items.LIME_CONCRETE);
			add(Items.GREEN_CONCRETE);
			add(Items.CYAN_CONCRETE);
			add(Items.LIGHT_BLUE_CONCRETE);
			add(Items.BLUE_CONCRETE);
			add(Items.PINK_CONCRETE);
			add(Items.MAGENTA_CONCRETE);
			add(Items.PURPLE_CONCRETE);
		}}));
		register(new Scroll("concrete_powder", new ArrayList<>(){{
			add(Items.WHITE_CONCRETE_POWDER);
			add(Items.LIGHT_GRAY_CONCRETE_POWDER);
			add(Items.GRAY_CONCRETE_POWDER);
			add(Items.BLACK_CONCRETE_POWDER);
			add(Items.RED_CONCRETE_POWDER);
			add(Items.BROWN_CONCRETE_POWDER);
			add(Items.ORANGE_CONCRETE_POWDER);
			add(Items.YELLOW_CONCRETE_POWDER);
			add(Items.LIME_CONCRETE_POWDER);
			add(Items.GREEN_CONCRETE_POWDER);
			add(Items.CYAN_CONCRETE_POWDER);
			add(Items.LIGHT_BLUE_CONCRETE_POWDER);
			add(Items.BLUE_CONCRETE_POWDER);
			add(Items.PINK_CONCRETE_POWDER);
			add(Items.MAGENTA_CONCRETE_POWDER);
			add(Items.PURPLE_CONCRETE_POWDER);
		}}));
		register(new Scroll("terracotta", new ArrayList<>(){{
			add(Items.WHITE_TERRACOTTA);
			add(Items.LIGHT_GRAY_TERRACOTTA);
			add(Items.GRAY_TERRACOTTA);
			add(Items.BLACK_TERRACOTTA);
			add(Items.RED_TERRACOTTA);
			add(Items.BROWN_TERRACOTTA);
			add(Items.ORANGE_TERRACOTTA);
			add(Items.YELLOW_TERRACOTTA);
			add(Items.LIME_TERRACOTTA);
			add(Items.GREEN_TERRACOTTA);
			add(Items.CYAN_TERRACOTTA);
			add(Items.LIGHT_BLUE_TERRACOTTA);
			add(Items.BLUE_TERRACOTTA);
			add(Items.PINK_TERRACOTTA);
			add(Items.MAGENTA_TERRACOTTA);
			add(Items.PURPLE_TERRACOTTA);
		}}));
		register(new Scroll("glazed_terracotta", new ArrayList<>(){{
			add(Items.WHITE_GLAZED_TERRACOTTA);
			add(Items.LIGHT_GRAY_GLAZED_TERRACOTTA);
			add(Items.GRAY_GLAZED_TERRACOTTA);
			add(Items.BLACK_GLAZED_TERRACOTTA);
			add(Items.RED_GLAZED_TERRACOTTA);
			add(Items.BROWN_GLAZED_TERRACOTTA);
			add(Items.ORANGE_GLAZED_TERRACOTTA);
			add(Items.YELLOW_GLAZED_TERRACOTTA);
			add(Items.LIME_GLAZED_TERRACOTTA);
			add(Items.GREEN_GLAZED_TERRACOTTA);
			add(Items.CYAN_GLAZED_TERRACOTTA);
			add(Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
			add(Items.BLUE_GLAZED_TERRACOTTA);
			add(Items.PINK_GLAZED_TERRACOTTA);
			add(Items.MAGENTA_GLAZED_TERRACOTTA);
			add(Items.PURPLE_GLAZED_TERRACOTTA);
		}}));
		register(new Scroll("stained_glass", new ArrayList<>(){{
			add(Items.WHITE_STAINED_GLASS);
			add(Items.LIGHT_GRAY_STAINED_GLASS);
			add(Items.GRAY_STAINED_GLASS);
			add(Items.BLACK_STAINED_GLASS);
			add(Items.RED_STAINED_GLASS);
			add(Items.BROWN_STAINED_GLASS);
			add(Items.ORANGE_STAINED_GLASS);
			add(Items.YELLOW_STAINED_GLASS);
			add(Items.LIME_STAINED_GLASS);
			add(Items.GREEN_STAINED_GLASS);
			add(Items.CYAN_STAINED_GLASS);
			add(Items.LIGHT_BLUE_STAINED_GLASS);
			add(Items.BLUE_STAINED_GLASS);
			add(Items.PINK_STAINED_GLASS);
			add(Items.MAGENTA_STAINED_GLASS);
			add(Items.PURPLE_STAINED_GLASS);
		}}));
		register(new Scroll("stained_glass_pane", new ArrayList<>(){{
			add(Items.WHITE_STAINED_GLASS_PANE);
			add(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
			add(Items.GRAY_STAINED_GLASS_PANE);
			add(Items.BLACK_STAINED_GLASS_PANE);
			add(Items.RED_STAINED_GLASS_PANE);
			add(Items.BROWN_STAINED_GLASS_PANE);
			add(Items.ORANGE_STAINED_GLASS_PANE);
			add(Items.YELLOW_STAINED_GLASS_PANE);
			add(Items.LIME_STAINED_GLASS_PANE);
			add(Items.GREEN_STAINED_GLASS_PANE);
			add(Items.CYAN_STAINED_GLASS_PANE);
			add(Items.LIGHT_BLUE_STAINED_GLASS_PANE);
			add(Items.BLUE_STAINED_GLASS_PANE);
			add(Items.PINK_STAINED_GLASS_PANE);
			add(Items.MAGENTA_STAINED_GLASS_PANE);
			add(Items.PURPLE_STAINED_GLASS_PANE);
		}}));
		
		save();
	}
}
