package net.anware.minecraft.mods.colourscroller.ui.components;

import net.anware.minecraft.mods.colourscroller.ui.Component;
import net.anware.minecraft.mods.colourscroller.ui.Tile;

public class Clickable<T extends Tile> extends Component<T> {
	public Clickable(T parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);
	}
	
	protected boolean enabled = true;
	
	@Override
	public final boolean clicked(double mouse_x, double mouse_y) {
		if (!this.enabled) {
			return false;
		}
		if (!this.hovered()) {
			return false;
		}
		Tile.playButtonSound();
		return this.clicked();
	}
	
	protected boolean clicked() {
		return false;
	}
	
	public boolean enabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
