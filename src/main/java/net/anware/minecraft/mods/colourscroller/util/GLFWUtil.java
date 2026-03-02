package net.anware.minecraft.mods.colourscroller.util;

import org.lwjgl.glfw.GLFW;

public class GLFWUtil {
	public static String getKeyName(int key) {
		String name = GLFW.glfwGetKeyName(key, 0);
		if (name != null) {
			return name.toUpperCase();
		}
		return switch (key) {
			// common
			case GLFW.GLFW_KEY_SPACE -> "SPACE";
			case GLFW.GLFW_KEY_LEFT_CONTROL -> "L CTRL";
			case GLFW.GLFW_KEY_RIGHT_CONTROL -> "R CTRL";
			case GLFW.GLFW_KEY_LEFT_ALT -> "L ALT";
			case GLFW.GLFW_KEY_RIGHT_ALT -> "R ALT";
			case GLFW.GLFW_KEY_LEFT_SHIFT -> "L SHIFT";
			case GLFW.GLFW_KEY_RIGHT_SHIFT -> "R SHIFT";
			// other
			case GLFW.GLFW_KEY_ESCAPE -> "ESCAPE";
			case GLFW.GLFW_KEY_ENTER -> "ENTER";
			case GLFW.GLFW_KEY_TAB -> "TAB";
			case GLFW.GLFW_KEY_BACKSPACE -> "BACKSPACE";
			case GLFW.GLFW_KEY_INSERT -> "INSERT";
			case GLFW.GLFW_KEY_DELETE -> "DELETE";
			case GLFW.GLFW_KEY_RIGHT -> "RIGHT";
			case GLFW.GLFW_KEY_LEFT -> "LEFT";
			case GLFW.GLFW_KEY_DOWN -> "DOWN";
			case GLFW.GLFW_KEY_UP -> "UP";
			case GLFW.GLFW_KEY_PAGE_UP -> "PAGE_UP";
			case GLFW.GLFW_KEY_PAGE_DOWN -> "PAGE_DOWN";
			case GLFW.GLFW_KEY_HOME -> "HOME";
			case GLFW.GLFW_KEY_END -> "END";
			case GLFW.GLFW_KEY_CAPS_LOCK -> "CAPS_LOCK";
			case GLFW.GLFW_KEY_SCROLL_LOCK -> "SCROLL_LOCK";
			case GLFW.GLFW_KEY_NUM_LOCK -> "NUM_LOCK";
			case GLFW.GLFW_KEY_PRINT_SCREEN -> "PRINT_SCREEN";
			case GLFW.GLFW_KEY_PAUSE -> "PAUSE";
			// function keys
			case GLFW.GLFW_KEY_F1 -> "F1";
			case GLFW.GLFW_KEY_F2 -> "F2";
			case GLFW.GLFW_KEY_F3 -> "F3";
			case GLFW.GLFW_KEY_F4 -> "F4";
			case GLFW.GLFW_KEY_F5 -> "F5";
			case GLFW.GLFW_KEY_F6 -> "F6";
			case GLFW.GLFW_KEY_F7 -> "F7";
			case GLFW.GLFW_KEY_F8 -> "F8";
			case GLFW.GLFW_KEY_F9 -> "F9";
			case GLFW.GLFW_KEY_F10 -> "F10";
			case GLFW.GLFW_KEY_F11 -> "F11";
			case GLFW.GLFW_KEY_F12 -> "F12";
			// unknown
			default -> "UNKNOWN_KEY(" + key + ")";
		};
	}
}
