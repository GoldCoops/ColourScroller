package net.anware.minecraft.mods.colourscroller.ui.tiles;

import net.anware.minecraft.mods.colourscroller.scroll.Scroll;
import net.anware.minecraft.mods.colourscroller.scroll.ScrollLookup;
import net.anware.minecraft.mods.colourscroller.ui.Tile;
import net.anware.minecraft.mods.colourscroller.ui.TileScreen;
import net.anware.minecraft.mods.colourscroller.ui.components.Clickable;
import net.anware.minecraft.mods.colourscroller.ui.components.TextBox;
import net.anware.minecraft.mods.colourscroller.util.GameUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import org.lwjgl.glfw.GLFW;

public class ScrollTile extends Tile {
	public ScrollTile(TileScreen parent, int x, int padTop, int padBottom, Scroll scroll, int width, int lineHeight) {
		super(parent, x, padTop, padBottom);
		this.scroll = scroll;
		this.width = width;
		this.lineHeight = lineHeight;
	}
	
	public static final int
		ID_BUTTON       = 0xF000;
	public static final int
		TITLE_HEIGHT    = 20;
	protected final Scroll scroll;
	protected final int width, lineHeight;
	protected int activeButton = -1;
	protected Clickable<ScrollTile> removeButton;
	protected TextBox<ScrollTile> nameTextBox;
	protected TextBox<ScrollTile> newItemTextBox;
	
	@Override
	public void init() {
		this.removeButton = new Clickable<>(this, this.width - TITLE_HEIGHT, 0, TITLE_HEIGHT, TITLE_HEIGHT) {
			@Override
			protected boolean clicked() {
				ScrollLookup.deleteScroll(this.parent.scroll);
				this.parent.parent.removeTiles(this.parent);
				return false;
			}
			
			@Override
			public void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
				int x = this.x(), y = this.y();
				if (this.hover) {
					this.parent.drawBox(matrices, x, x + this.w, y, y + this.h, 0xEEFF2020);
				}
				this.parent.drawLineCenteredH(matrices, x + this.w / 2, y + this.h / 2, 6, 0xDDFFFFFF);
			}
		};
		
		this.nameTextBox = new TextBox<>(this, 35, 2, 120, TITLE_HEIGHT - 4, this.scroll.getId()) {
			@Override
			protected boolean canEnter(char c) {
				return (c >= 'a' && c <= 'z') || c == '_';
			}
			
			@Override
			public void setActive(boolean active) {
				super.setActive(active);
				if (!active) {
					ScrollLookup.changeScrollID(this.parent.scroll, this.getText());
				}
			}
		};
		
		this.newItemTextBox = new TextBox<>(this, 2, TITLE_HEIGHT + this.scroll.size() * this.lineHeight + 2, 200, this.lineHeight - 4, "") {
			@Override
			protected boolean canEnter(char c) {
				return (c >= 'a' && c <= 'z') || c == '_' || c == ':';
			}
		};
		
		super.init();
	}
	
	@Override
	public void set_y(int y) {
		super.set_y(y);
		if (this.newItemTextBox != null) {
			this.newItemTextBox.set_y(TITLE_HEIGHT + this.scroll.size() * this.lineHeight + 2);
		}
	}
	
	@Override
	protected void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		this.drawBox(matrices, this.x, this.x + this.width, this.y, this.y + this.getContentHeight(), 0xAA202020, 0xFF000000);
		this.drawBox(matrices, this.x, this.x + this.width, this.y, this.y + TITLE_HEIGHT, 0xEE202020);
		this.drawText(matrices, "Name:", this.x + 4, this.y + TITLE_HEIGHT - 4, this.activeButton == ID_BUTTON ? 0xFF707070 : 0xFFFFFFFF, Tile.ALIGN_LEFT);
		
		if (this.activeButton != -1) {
			if (this.activeButton == ID_BUTTON) {
				this.drawBox(matrices, this.x + 2, this.x + 100, this.y + 2, this.y + TITLE_HEIGHT - 2, 0xFFFFFFFF);
			} else {
				int base_y = this.y + TITLE_HEIGHT + this.activeButton * this.lineHeight;
				this.drawBox(matrices, this.x, this.x + this.width, base_y, base_y + this.lineHeight, 0xEEFFFFFF);
			}
		}
		
		int hoveredButton = this.getItemIndexAt(mouse_x, mouse_y);
		boolean left = this.x + this.width - mouse_x > this.lineHeight;
		if (hoveredButton != -1) {
			if (left) {
				if (hoveredButton != this.scroll.size()) {
					int base_y = this.y + TITLE_HEIGHT + hoveredButton * this.lineHeight;
					this.drawBox(matrices, this.x, this.x + this.width, base_y, base_y + this.lineHeight, 0xBBBBBBBB);
				}
			} else {
				int colour = hoveredButton == this.scroll.size() ? 0xEE20FF20 : 0xEEFF2020;
				int base_y = this.y + TITLE_HEIGHT + hoveredButton * this.lineHeight;
				this.drawBox(matrices, this.x + this.width - this.lineHeight, this.x + this.width, base_y, base_y + this.lineHeight, colour);
			}
		}
		
		for (int i = 0; i < this.scroll.size(); i++) {
			Item item = this.scroll.getItem(i);
			int base_y = this.y + TITLE_HEIGHT + i * this.lineHeight;
			this.drawItemCentered(item, this.x + this.lineHeight / 2 + 3, base_y + this.lineHeight / 2, 1.0f);
			this.drawText(matrices, GameUtil.getName(item), this.x + this.lineHeight + 6, base_y + (float) this.lineHeight / 2, this.activeButton == i ? 0xFF707070 : 0xFFFFFFFF, Tile.ALIGN_MID_V);
			this.drawLineCenteredH(matrices, this.x + this.width - this.lineHeight / 2, this.y + TITLE_HEIGHT + i * this.lineHeight + this.lineHeight / 2, 6, 0xDDFFFFFF);
		}
		
		this.drawLineCenteredH(matrices, this.x + this.width - this.lineHeight / 2, this.y + TITLE_HEIGHT + this.scroll.size() * this.lineHeight + this.lineHeight / 2, 6, 0xDDFFFFFF);
		this.drawLineCenteredV(matrices, this.x + this.width - this.lineHeight / 2, this.y + TITLE_HEIGHT + this.scroll.size() * this.lineHeight + this.lineHeight / 2, 6, 0xDDFFFFFF);
	}
	
	@Override
	public int getContentHeight() {
		return (this.scroll.size() + 1) * this.lineHeight + TITLE_HEIGHT;
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (!active) {
			this.activeButton = -1;
		}
		if (!active || this.activeComponent != this.newItemTextBox) {
			this.newItemTextBox.clear();
		}
	}
	
	@Override
	protected boolean clicked(double mouse_x, double mouse_y) {
		int button = this.getItemIndexAt((int) mouse_x, (int) mouse_y);
		if (button != -1) {
			boolean left = this.x + this.width - mouse_x > this.lineHeight;
			if (button == this.scroll.size()) {
				if (left) return false;
				Item item = GameUtil.getItem(this.newItemTextBox.getText());
				if (item != null) {
					ScrollLookup.addItem(this.scroll, item);
					this.parent.arrangeTiles();
				}
				playButtonSound();
				return false;
			}
			playButtonSound();
			if (left) {
				this.activeButton = button;
				return true;
			} else {
				this.activeButton = -1;
				this.scroll.deleteItem(button);
				this.parent.arrangeTiles();
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean onKey(int key) {
		if (this.activeButton != -1 && this.activeButton != ID_BUTTON) {
			if (key == GLFW.GLFW_KEY_UP && this.activeButton > 0) {
				this.scroll.shiftItem(this.activeButton, -1);
				this.activeButton -= 1;
			} else if (key == GLFW.GLFW_KEY_DOWN && this.activeButton < this.scroll.size() - 1) {
				this.scroll.shiftItem(this.activeButton, 1);
				this.activeButton += 1;
			} else {
				return false;
			}
			return true;
		}
		return false;
	}
	
	protected int getItemIndexAt(int x, int y) {
		if (x < this.x || x > this.x + this.width) {
			return -1;
		}
		int i = Math.floorDiv(y - TITLE_HEIGHT - this.y, this.lineHeight);
		if (i < 0 || i > this.scroll.size()) {
			return -1;
		}
		return i;
	}
}