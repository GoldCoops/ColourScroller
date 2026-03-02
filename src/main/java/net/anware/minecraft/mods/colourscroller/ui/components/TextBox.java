package net.anware.minecraft.mods.colourscroller.ui.components;

import net.anware.minecraft.mods.colourscroller.keybind.KeyWrapper;
import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class TextBox<T extends Tile> extends Clickable<T> {
	public TextBox(T parent, int x, int y, int w, int h, String text) {
		super(parent, x, y, w, h);
		this.text = new StringBuilder(text);
		this.setTextWidth();
	}
	
	private StringBuilder text;
	private int textWidth = 0;
	
	@Override
	public void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		int fill = 0x00000000;
		if (this.active) {
			fill = 0xFF000000;
		} else if (this.hover) {
			fill = 0xBB000000;
		}
		this.parent.drawBox(matrices, this.x(), this.x() + this.w, this.y(), this.y() + this.h, fill, 0xFF404040);
		this.parent.drawText(matrices, this.text.toString(), this.x() + 2, this.y() + this.h - 2, 0xFFFFFFFF, Tile.ALIGN_LEFT);
		if (this.active) {
			int textEnd_x = this.x() + 2 + this.textWidth;
			this.parent.drawLineH(matrices, textEnd_x, textEnd_x + 4, this.y() + this.h - 2, 0xFFFFFFFF);
		}
	}
	
	@Override
	protected boolean clicked() {
		return true;
	}
	
	@Override
	public boolean onKey(int key) {
		if (key == GLFW.GLFW_KEY_BACKSPACE && !this.text.isEmpty()) {
			if (KeyWrapper.INSTANCE.getKey(GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS) {
				this.clear();
			} else {
				this.text.deleteCharAt(this.text.length() - 1);
			}
			this.onTextChange();
			return true;
		}
		if (key == GLFW.GLFW_KEY_ENTER) {
			this.parent.parent.setActiveTile(null);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onTyped(char c) {
		if (!this.canEnter(c)) {
			return false;
		}
		if (this.textWidth >= this.w - 4) {
			return false;
		}
		this.text.append(c);
		this.onTextChange();
		return true;
	}
	
	public String getText() {
		return this.text.toString();
	}
	
	protected boolean canEnter(char c) {
		return true;
	}
	
	public void clear() {
		this.text = new StringBuilder();
		this.onTextChange();
	}
	
	protected void onTextChange() {
		this.setTextWidth();
	}
	
	protected void setTextWidth() {
		if (this.text.isEmpty()) {
			this.textWidth = 0;
		}
		this.textWidth = this.parent.parent.getTextRenderer().getWidth(new LiteralText(this.text.toString()));
	}
}
