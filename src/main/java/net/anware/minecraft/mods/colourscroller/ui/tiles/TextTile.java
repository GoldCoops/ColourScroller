package net.anware.minecraft.mods.colourscroller.ui.tiles;

import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.minecraft.client.util.math.MatrixStack;

public class TextTile extends Tile {
	public TextTile(TileScreen parent, int x, int padTop, int padBottom, String text, int colour) {
		super(parent, x, padTop, padBottom);
		this.text = text;
		this.colour = colour;
	}
	
	protected final String text;
	protected final int colour;
	
	@Override
	protected void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		this.drawText(matrices, this.text, this.x, this.y + (float) this.getContentHeight() / 2, this.colour, ALIGN_MID_V);
	}
	
	@Override
	public int getContentHeight() {
		return 8;
	}
}
