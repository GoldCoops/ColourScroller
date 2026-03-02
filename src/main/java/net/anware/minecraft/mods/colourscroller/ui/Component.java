package net.anware.minecraft.mods.colourscroller.ui;

import net.minecraft.client.util.math.MatrixStack;

public class Component<T extends Tile> {
	public Component(T parent, int x, int y, int w, int h) {
		this.parent = parent;
		this.parent.addComponent(this);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	protected final T parent;
	protected boolean active = false, hover = false;
	private int x, y;
	protected int w, h;
	
	/* -------------------- Getter / Setter ------------------------- */
	
	public int x() {
		return this.x + this.parent.get_x();
	}
	
	public int y() {
		return this.y + this.parent.get_y();
	}
	
	public void set_x(int x) {
		this.x = x;
	}
	
	public void set_y(int y) {
		this.y = y;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setHover(int mouse_x, int mouse_y) {
		boolean x = mouse_x >= this.x() && mouse_x <= this.x() + this.w;
		boolean y = mouse_y >= this.y() && mouse_y <= this.y() + this.h;
		this.hover = x && y;
	}
	
	public boolean hovered() {
		return this.hover;
	}
	
	/* -------------------- Getter / Setter ------------------------- */
	
	public void init() {}
	
	public void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {}
	
	public boolean clicked(double mouse_x, double mouse_y) {
		return false;
	}
	
	public boolean onKey(int key) {
		return false;
	}
	
	public boolean onTyped(char c) {
		return false;
	}
}