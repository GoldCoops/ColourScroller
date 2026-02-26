package net.anware.tmc.colourscroller;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    public static final List<KeyBinding> KEY_REG = new ArrayList<>();
    public static final KeyBinding KEY_BASE = createKey("base", GLFW.GLFW_KEY_LEFT_CONTROL);
    public static final KeyBinding KEY_SCROLL_SINGLE = createKey("scroll_single", GLFW.GLFW_KEY_X);
    public static final KeyBinding KEY_SCROLL_ROW = createKey("scroll_row", GLFW.GLFW_KEY_C);

    public static void register() {
        for (KeyBinding keybind : KEY_REG) KeyBindingHelper.registerKeyBinding(keybind);
    }

    public static KeyBinding createKey(String name, int key) {
        KeyBinding keybind = new KeyBinding("key." + ColourScroller.ID + "." + name, key, "key.categories." + ColourScroller.ID);
        KEY_REG.add(keybind);
        return keybind;
    }
}