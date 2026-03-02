package net.anware.minecraft.mods.colourscroller.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.anware.minecraft.mods.colourscroller.util.GameUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
	public Tile(TileScreen parent, int x, int padTop, int padBottom) {
		this.parent = parent;
		this.padTop = padTop;
		this.padBottom = padBottom;
		this.x = x;
	}
	
	protected final List<Component<?>> components = new ArrayList<>();
	protected Component<?> activeComponent = null;
	protected int x, y = 0;
	private boolean active = false;
	public final TileScreen parent;
	public final int padTop, padBottom;
	
	/* -------------------- Components ------------------------- */
	
	public void addComponent(Component<?> component) {
		this.components.add(component);
	}
	
	public Component<?> getActiveComponent() {
		return this.activeComponent;
	}
	
	public void setActiveComponent(Component<?> component) {
		if (component == this.activeComponent) {
			return;
		}
		if (this.activeComponent != null) {
			this.activeComponent.setActive(false);
		}
		if (component != null) {
			component.setActive(true);
		}
		this.activeComponent = component;
	}
	
	/* -------------------- Actions ------------------------- */
	
	public void init() {
		for (Component<?> component : this.components) {
			component.init();
		}
	}
	
	public void invokeDraw(MatrixStack matrices, int mouse_x, int mouse_y, float delta) {
		this.draw(matrices, mouse_x, mouse_y, delta);
		for (Component<?> component : this.components) {
			component.setHover(mouse_x, mouse_y);
			component.draw(matrices, mouse_x, mouse_y, delta);
		}
	}
	
	public boolean invokeClicked(double mouse_x, double mouse_y) {
		for (Component<?> component : this.components) {
			if (component.clicked(mouse_x, mouse_y)) {
				this.setActiveComponent(component);
				return true;
			}
		}
		this.setActiveComponent(null);
		return this.clicked(mouse_x, mouse_y);
	}
	
	public boolean invokeOnKey(int key) {
		if (this.activeComponent != null) {
			if (this.activeComponent.onKey(key)) {
				return true;
			}
		}
		return this.onKey(key);
	}
	
	public boolean invokeOnTyped(char c) {
		if (this.activeComponent != null) {
			if (this.activeComponent.onTyped(c)) {
				return true;
			}
		}
		return this.onTyped(c);
	}
	
	protected abstract void draw(MatrixStack matrices, int mouse_x, int mouse_y, float delta);
	
	protected boolean onKey(int key) {
		return false;
	}
	
	protected boolean onTyped(char c) {
		return false;
	}
	
	protected boolean clicked(double mouse_x, double mouse_y) {
		return false;
	}
	
	/* -------------------- Getter / Setter ------------------------- */
	
	public boolean active() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		if (!active && this.activeComponent != null) {
			this.setActiveComponent(null);
		}
	}
	
	public void set_y(int y) {
		this.y = y;
	}
	
	public int get_x() {
		return this.x;
	}
	
	public int get_y() {
		return this.y;
	}
	
	public abstract int getContentHeight();
	
	public final int getPageHeight() {
		return this.getContentHeight() + this.padTop + this.padBottom;
	}
	
	/* -------------------- Functions ------------------------- */
	
	public static final int
		ALIGN_MID_H     = 0x0001,
		ALIGN_MID_V     = 0x0002,
		ALIGN_CENTER    = 0x0003,
		ALIGN_LEFT      = 0x0000;
	public static final int
		STATE_DISABLED  = 0x0000,
		STATE_IDLE      = 0x0001,
		STATE_HOVER     = 0x0002,
		STATE_ACTIVE    = 0x0004;
	public static final ItemRenderer ITEM_RENDERER = MinecraftClient.getInstance().getItemRenderer();
	public static final int ITEM_SIZE = 16;
	
	public static void playButtonSound() {
		GameUtil.CLIENT.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 0.8F));
	}
	
	public final void drawText(MatrixStack matrices, String text, float x, float y, int colour, int alignment) {
		TextRenderer textRenderer = this.parent.getTextRenderer();
		if (textRenderer == null || text == null) {
			return;
		}
		OrderedText drawText = new LiteralText(text).asOrderedText();
		if ((alignment & ALIGN_MID_H) != 0) {
			x = x - (float) textRenderer.getWidth(drawText) / 2;
		}
		if ((alignment & ALIGN_MID_V) != 0) {
			y -= 4;
		} else {
			y -= 8;
		}
		textRenderer.drawWithShadow(matrices, drawText, x, y, colour);
	}
	
	public final void drawBox(MatrixStack matrices, int x, int x1, int y, int y1, int fillColour) {
		DrawableHelper.fill(matrices, x, y, x1, y1, fillColour);
	}
	
	public final void drawBox(MatrixStack matrices, int x, int x1, int y, int y1, int fillColour, int outlineColour) {
		DrawableHelper.fill(matrices, x, y, x1, y1, fillColour);
		drawLineH(matrices, x, x1, y, outlineColour);
		drawLineH(matrices, x, x1, y1, outlineColour);
		drawLineV(matrices, x, y, y1, outlineColour);
		drawLineV(matrices, x1, y, y1, outlineColour);
	}
	
	public final void drawLineV(MatrixStack matrices, int x, int y, int y1, int colour) {
		if (y1 < y) {
			int i = y;
			y = y1;
			y1 = i;
		}
		DrawableHelper.fill(matrices, x, y + 1, x + 1, y1, colour);
	}
	
	public final void drawLineH(MatrixStack matrices, int x, int x1, int y, int colour) {
		if (x1 < x) {
			int i = x;
			x = x1;
			x1 = i;
		}
		DrawableHelper.fill(matrices, x, y, x1 + 1, y + 1, colour);
	}
	
	public final void drawLineCenteredH(MatrixStack matrices, int x, int y, int len, int colour) {
		drawLineH(matrices, x - len / 2, x + len / 2, y, colour);
	}
	
	public final void drawLineCenteredV(MatrixStack matrices, int x, int y, int len, int colour) {
		drawLineV(matrices, x, y - len / 2, y + len / 2, colour);
	}
	
	public final void drawItem(Item item, int x, int y, float scale) {
		MatrixStack matrix = RenderSystem.getModelViewStack();
		matrix.push();
		matrix.translate(x, y - this.parent.animScroll, 0);
		matrix.scale(scale, scale, scale);
		ITEM_RENDERER.renderInGuiWithOverrides(new ItemStack(item), 0, 0);
		matrix.pop();
		RenderSystem.applyModelViewMatrix();
	}
	
	public final void drawItemCentered(Item item, int x, int y, float scale) {
		this.drawItem(item, Math.round(x - 8 * scale), Math.round(y - 8 * scale), scale);
	}
}
