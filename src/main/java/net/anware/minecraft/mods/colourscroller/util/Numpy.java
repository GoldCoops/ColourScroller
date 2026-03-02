package net.anware.minecraft.mods.colourscroller.util;

public class Numpy {
	
	public static int roundIndex(int index, int size) {
		if (index < 0) {
			index = index * (1 - size);
		}
		return index % size;
	}
	
	public static int clamp(int val, int min, int max) {
		if (val < min) {
			return min;
		}
		if (val > max) {
			return max;
		}
		return val;
	}
	
	public static int round(double val) {
		return (int) Math.round(val);
	}
}
