package net.anware.minecraft.mods.colourscroller.ui;

import net.anware.minecraft.mods.colourscroller.util.Numpy;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TileScreen extends Screen {
	public TileScreen(String title, int padding) {
		super(new LiteralText(title));
		this.padding = padding;
	}
	
	protected final List<Tile> tiles = new ArrayList<>();
	protected Tile activeTile = null;
	protected int pageHeight = 0;
	protected double scroll = 0.0, animScroll = 0.0;
	protected final int padding;
	
	/* -------------------- Tiles ------------------------- */
	
	private boolean dirty = false;
	private final List<Tile> markAdd = new ArrayList<>();
	private final List<Tile> markRemove = new ArrayList<>();
	
	public void arrangeTiles() {
		int y = this.padding;
		for (Tile tile : this.tiles) {
			tile.set_y(y + tile.padTop);
			y += tile.getPageHeight();
		}
		this.pageHeight = y + this.padding;
	}
	
	public void addTiles(List<Tile> tiles) {
		this.markAdd.addAll(tiles);
		this.dirty = true;
	}
	
	public void addTiles(Tile... tiles) {
		this.markAdd.addAll(List.of(tiles));
		this.dirty = true;
	}
	
	public void removeTiles(Tile... tiles) {
		this.markRemove.addAll(List.of(tiles));
		this.dirty = true;
	}
	
	public void flushChanges() {
		// clean up tile changes
		for (Tile t : this.markRemove) {
			this.tiles.remove(t);
		}
		this.tiles.addAll(this.markAdd);
		this.markRemove.clear();
		this.markAdd.clear();
		this.arrangeTiles();
		
		// check scroll
		int maxScroll = this.pageHeight - this.height;
		if (maxScroll < 0) {
			this.scroll = 0;
			this.animScroll = 0;
		} else {
			this.scroll = Numpy.clamp((int) this.scroll, 0, maxScroll);
			this.animScroll = scroll;
		}
		
		// mark clean
		this.dirty = false;
	}
	
	public Tile getActiveTile() {
		return this.activeTile;
	}
	
	public void setActiveTile(Tile tile) {
		if (this.activeTile == tile) {
			return;
		}
		if (this.activeTile != null) {
			this.activeTile.setActive(false);
		}
		if (tile != null) {
			tile.setActive(true);
		}
		this.activeTile = tile;
	}
	
	/* -------------------- Actions ------------------------- */
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		
		// flush changes to tile elements
		if (this.dirty) {
			this.flushChanges();
		}
		
		// update & apply scroll animation
		this.animScroll = this.scroll * 0.1 + this.animScroll * 0.9;
		if (Math.abs(this.animScroll) < 0.01) this.animScroll = 0.0;
		
		// draw scroll bar
		if (this.animScroll != 0) {
			int x0 = this.width - 8;
			int x1 = this.width;
			int y0 = Numpy.round((this.animScroll / this.pageHeight) * this.height);
			int y1 = Numpy.round(((this.animScroll + this.height) / this.pageHeight) * this.height);
			
			DrawableHelper.fill(matrices, x0 + 1, y0 + 2, x1, y1, 0xDD202020);
			DrawableHelper.fill(matrices, x0, y0, x1 - 2, y1 - 2, 0xFFFFFFFF);
		}
		
		// apply scroll effect and draw
		matrices.push();
		matrices.translate(0, -this.animScroll, 0);
		mouseY += Numpy.round(this.animScroll);
		for (Tile tile : this.tiles) {
			tile.invokeDraw(matrices, mouseX, mouseY, delta);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		mouseY += Numpy.round(this.animScroll);
		for (Tile tile : this.tiles) {
			if (tile.invokeClicked(mouseX, mouseY)) {
				this.setActiveTile(tile);
				return true;
			}
		}
		this.setActiveTile(null);
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (this.height < this.pageHeight) {
			this.scroll = Numpy.clamp((int) Math.round(this.scroll - amount * 50), 0, this.pageHeight - this.height);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			if (this.activeTile == null) {
				this.onClose();
				return true;
			}
			this.setActiveTile(null);
			return true;
		}
		if (this.activeTile != null) {
			return this.activeTile.invokeOnKey(keyCode);
		}
		return false;
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		if (this.activeTile != null) {
			if (this.activeTile.invokeOnTyped(chr)) {
				return true;
			}
		}
		return false;
	}
	
	/* -------------------- Inherited ------------------------- */
	
	@Override
	public void init() {
		this.flushChanges();
		this.arrangeTiles();
		for (Tile tile : this.tiles) {
			tile.init();
		}
	}
	
	public TextRenderer getTextRenderer() {
		return this.textRenderer;
	}
}
