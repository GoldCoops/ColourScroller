package net.anware.minecraft.mods.colourscroller.keybind;

import net.anware.minecraft.mods.colourscroller.util.GLFWUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*
	Stores and handles key sequence data.
	This class is supposed to be immutable for easier use,
	 so when changing key sequence for a keybinding,
	  assign a new keySequence object to it instead of modifying the data.
 */

public class KeySequence implements Iterable<Integer> {
	public KeySequence(List<Integer> keySequence) {
		this.keySequence = new ArrayList<>(keySequence.size());
		for (int k : keySequence) if (!this.keySequence.contains(k)) this.keySequence.add(k);
	}
	public KeySequence(int... keySequence) {
		this.keySequence = new ArrayList<>(keySequence.length);
		for (int k : keySequence) if (!this.keySequence.contains(k)) this.keySequence.add(k);
	}
	
	protected final List<Integer> keySequence;
	
	public int length() {
		return this.keySequence.size();
	}
	
	public int indexOf(int key) {
		return this.keySequence.indexOf(key);
	}
	
	public List<Integer> get() {
		return this.keySequence;
	}
	
	public static String formatKeySequence(List<Integer> sequence) {
		return sequence.stream().map(GLFWUtil::getKeyName).collect(Collectors.joining(" + "));
	}
	
	@Override
	public @NotNull Iterator<Integer> iterator() {
		return this.keySequence.iterator();
	}
	
	@Override
	public String toString() {
		return formatKeySequence(this.keySequence);
	}
}
