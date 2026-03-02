package net.anware.minecraft.mods.colourscroller.keybind;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/*
    wraps key press from GLFW
 */

public class KeyWrapper {

    public long window;
    public GLFWKeyCallbackI callbackI;
    public static final KeyWrapper INSTANCE = new KeyWrapper();

    public void wrap(MinecraftClient client) {
        // store ref
        this.window = client.getWindow().getHandle();
        this.callbackI = GLFW.glfwSetKeyCallback(this.window, null);
        
        // wrap key event
        GLFW.glfwSetKeyCallback(this.window, (win, key, scancode, action, mods) -> {
            KeyBindLookup.onKeyEvent(win, key, scancode, action, mods);
            if (this.callbackI != null) {
                this.callbackI.invoke(win, key, scancode, action, mods);
            }
        });
    }
    
    public int getKey(int key) {
        return GLFW.glfwGetKey(this.window, key);
    }
}