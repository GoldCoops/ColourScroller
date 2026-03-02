package net.anware.minecraft.mods.colourscroller.ui.components;

import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Button<T extends Tile> extends Clickable<T> {
	public Button(T parent, int x, int y, int w, int h, String text) {
		super(parent, x, y, w, h);
		this.text = text;
	}
	
	protected String text;
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		int x = this.x();
		int y = this.y();
		
		int outlineColour = 0xFF202020;
		if (this.active)
			outlineColour = 0xFFFFFFFF;
		else if (this.hovered())
			outlineColour = 0xFF606060;
		this.parent.drawBox(matrices, x, x + this.w, y, y + this.h, 0xAA202020, outlineColour);
		
		TextRenderer textRenderer = this.parent.parent.getTextRenderer();
		int textColour = this.enabled ? 0xFFFFFFFF : 0xFF303030;
		if (textRenderer != null && text != null) {
			this.parent.drawText(matrices, this.text, x + (float) this.w / 2, y + (float) this.h / 2, textColour, Tile.ALIGN_CENTER);
		}
	}
}
