package net.anware.minecraft.mods.colourscroller.ui.tiles;

import net.anware.minecraft.mods.colourscroller.scroll.Scroll;
import net.anware.minecraft.mods.colourscroller.scroll.ScrollLookup;
import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.anware.minecraft.mods.colourscroller.ui.components.Clickable;
import net.anware.minecraft.mods.colourscroller.ui.components.TextBox;
import net.anware.minecraft.mods.colourscroller.ui.screens.ScrollScreen;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public class AddScrollTile extends Tile {
	public AddScrollTile(TileScreen parent, int x, int padTop, int padBottom) {
		super(parent, x, padTop, padBottom);
	}
	
	public static final int W = 250, H = 20;
	protected TextBox<AddScrollTile> nameTextBox;
	protected Clickable<AddScrollTile> addButton;
	
	@Override
	public void init() {
		this.nameTextBox = new TextBox<>(this, 2, 2, W - H - 4, H - 4, "");
		this.addButton = new Clickable<>(this, W - H, 0, H, H) {
			@Override
			public void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
				int colour = this.hovered() ? 0xEE20FF20 : 0xBB202020;
				this.parent.drawBox(matrices, this.x(), this.x() + this.w, this.y(), this.y() + this.h, colour);
				this.parent.drawLineCenteredH(matrices, this.x() + this.w / 2, this.y() + this.h / 2, 6, 0xDDFFFFFF);
				this.parent.drawLineCenteredV(matrices, this.x() + this.w / 2, this.y() + this.h / 2, 6, 0xDDFFFFFF);
			}
			
			@Override
			protected boolean clicked() {
				String id = this.parent.nameTextBox.getText();
				Scroll scroll = new Scroll(id, List.of());
				ScrollLookup.register(scroll);
				scroll.save();
				ScrollScreen screen = (ScrollScreen) this.parent.parent;
				Tile tile = screen.getScrollTile(scroll);
				screen.addTiles(tile);
				tile.init();
				return true;
			}
		};
		super.init();
	}
	
	@Override
	protected void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		this.drawBox(matrices, this.x, this.x + W, this.y, this.y + H, 0xAA202020);
	}
	
	@Override
	public int getContentHeight() {
		return H;
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (!active || this.getActiveComponent() != this.nameTextBox) {
			this.nameTextBox.clear();
		}
	}
}
