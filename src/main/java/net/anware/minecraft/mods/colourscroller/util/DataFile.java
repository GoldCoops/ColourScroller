package net.anware.minecraft.mods.colourscroller.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.anware.minecraft.mods.colourscroller.Client;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataFile {
	public DataFile(Path path) {
		this.path = path;
		this.root = loadOrCreate(path);
	}
	
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(Client.MOD_ID);
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public final Path path;
	public final JsonObject root;
	
	/** Put a nested structure under a top-level key */
	public void put(String key, Object value) {
		root.add(key, toJsonValue(value));
		save();
	}
	
	/** Get a nested Json view for objects */
	public DataFile get(String key) {
		JsonElement el = root.get(key);
		if (el == null || !el.isJsonObject())
			throw new RuntimeException("Key '" + key + "' is not an object");
		return new DataFile(path, el.getAsJsonObject());
	}
	
	/** Get a list of strings (or nested structures) */
	public List<DataFile> getList(String key) {
		JsonElement el = root.get(key);
		if (el == null || !el.isJsonArray())
			throw new RuntimeException("Key '" + key + "' is not a list");
		
		List<DataFile> out = new ArrayList<>();
		for (JsonElement e : el.getAsJsonArray()) {
			if (!e.isJsonObject())
				throw new RuntimeException("List element is not an object");
			out.add(new DataFile(path, e.getAsJsonObject()));
		}
		return out;
	}
	
	/** Get map-like structure (String → Json) */
	public Map<String, DataFile> getMap(String key) {
		JsonElement el = root.get(key);
		if (el == null || !el.isJsonObject())
			throw new RuntimeException("Key '" + key + "' is not an object");
		
		Map<String, DataFile> map = new LinkedHashMap<>();
		for (Map.Entry<String, JsonElement> e : el.getAsJsonObject().entrySet()) {
			if (!e.getValue().isJsonObject())
				throw new RuntimeException("Map value is not an object");
			map.put(e.getKey(), new DataFile(path, e.getValue().getAsJsonObject()));
		}
		return map;
	}
	
	// ----------------------------------------- INTERNAL -----------------------------------------
	
	/** Private constructor for nested Json views */
	private DataFile(Path path, JsonObject internalRoot) {
		this.path = path;
		this.root = internalRoot;
	}
	
	/** Convert nested List/Map/String recursively into JsonElement */
	private JsonElement toJsonValue(Object value) {
		if (value == null)
			return JsonNull.INSTANCE;
		
		if (value instanceof Number n)
			return new JsonPrimitive(n);
		
		if (value instanceof Boolean b)
			return new JsonPrimitive(b);
		
		if (value instanceof Character c)
			return new JsonPrimitive(c);
		
		if (value instanceof String s)
			return new JsonPrimitive(s);

		
		if (value instanceof Map<?, ?> map) {
			JsonObject obj = new JsonObject();
			for (var entry : map.entrySet()) {
				if (!(entry.getKey() instanceof String))
					throw new RuntimeException("Map key must be a String");
				obj.add((String) entry.getKey(), toJsonValue(entry.getValue()));
			}
			return obj;
		}
		
		if (value instanceof List<?> list) {
			JsonArray arr = new JsonArray();
			for (Object o : list) {
				arr.add(toJsonValue(o));
			}
			return arr;
		}
		
		throw new RuntimeException("Invalid type: " + value.getClass());
	}
	
	private JsonObject loadOrCreate(Path path) {
		try {
			Files.createDirectories(path.getParent());
			if (Files.exists(path)) {
				try (Reader reader = Files.newBufferedReader(path)) {
					JsonParser parser = new JsonParser();
					JsonElement el = parser.parse(reader);
					if (el.isJsonObject()) {
						return el.getAsJsonObject();
					}
				}
			}
			// Create new file with empty object
			JsonObject obj = new JsonObject();
			saveObject(path, obj);
			return obj;
		} catch (IOException e) {
			throw new RuntimeException("Failed to load JSON: " + path, e);
		}
	}
	
	private void save() {
		saveObject(path, root);
	}
	
	private static void saveObject(Path path, JsonObject obj) {
		try (Writer writer = Files.newBufferedWriter(path)) {
			GSON.toJson(obj, writer);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write JSON: " + path, e);
		}
	}
}