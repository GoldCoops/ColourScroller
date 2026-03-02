package net.anware.minecraft.mods.colourscroller.scroll;

import net.anware.minecraft.mods.colourscroller.Client;
import net.anware.minecraft.mods.colourscroller.util.DataFile;
import net.anware.minecraft.mods.colourscroller.util.GameUtil;
import net.anware.minecraft.mods.colourscroller.util.Numpy;
import net.minecraft.item.Item;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.anware.minecraft.mods.colourscroller.scroll.ScrollLookup.SCROLL_FOLDER;

public class Scroll {
	public Scroll(String id, List<Item> items) {
		this.id = id;
		this.items = items;
	}
	public Scroll(String id, Item... items) {
		this.id = id;
		this.items = new ArrayList<>(List.of(items));
	}
	
	protected String id;
	protected final List<Item> items;
	
	public int size() {
		return this.items.size();
	}
	
	public String getId() {
		return this.id;
	}
	
	public Item getItem(int index) {
		return this.items.get(index);
	}
	
	public void deleteItem(int index) {
		Item item = this.items.get(index);
		ScrollLookup.deleteItem(item);
		this.items.remove(index);
		this.save();
	}
	
	public void shiftItem(int index, int shift) {
		int newIndex = index + shift;
		if (newIndex < 0 || newIndex >= this.size()) {
			return;
		}
		Item item = this.getItem(newIndex);
		this.items.set(newIndex, this.items.get(index));
		this.items.set(index, item);
		this.save();
	}
	
	public void addItem(Item item) {
		this.items.add(item);
		this.save();
	}
	
	public Item getShifted(Item item, int shift) {
		int index = this.items.indexOf(item);
		if (index == -1) {
			return null;
		}
		return this.items.get(Numpy.roundIndex(index + shift, this.items.size()));
	}
	
	public void save() {
		try {
			Files.createDirectories(SCROLL_FOLDER);
			DataFile file = new DataFile(SCROLL_FOLDER.resolve(this.id + ".json"));
			file.put("scroll", this.serialize());
		} catch (Exception e) {
			Client.printError("failed to save scroll folder: " + e);
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	public Object serialize() {
		Map<String, Object> e = new LinkedHashMap<>();
		e.put("id", id);
		
		List<String> itemIDs = new ArrayList<>();
		for (Item item : items) {
			itemIDs.add(GameUtil.getID(item).toString());
		}
		e.put("items", itemIDs);
		
		return e;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}