package engine.graph.hlib.graphics.paint;

import static org.lwjgl.nanovg.NanoVG.NVG_IMAGE_GENERATE_MIPMAPS;
import static org.lwjgl.nanovg.NanoVG.nvgCreateImage;
import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;
import static org.lwjgl.nanovg.NanoVG.nvgImagePattern;
import static org.lwjgl.nanovg.NanoVG.nvgImageSize;

import java.nio.IntBuffer;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.system.MemoryStack;

public class Image implements Paint2 {

	private int id;

	private int height;

	private int width;

	private final String filename;

	private NVGPaint image;

	public Image(final String filename) {
		this.filename = filename;
	}

	public void init(final long ctx, final int x, final int y) {
		if (id == 0) {
			id = nvgCreateImage(ctx, filename, NVG_IMAGE_GENERATE_MIPMAPS);
			image = NVGPaint.calloc();

			try (final MemoryStack stack = MemoryStack.stackPush()) {
				final IntBuffer h = stack.callocInt(1), w = stack.callocInt(1);

				nvgImageSize(ctx, id, w, h);

				width = w.get();
				height = h.get();
				
				nvgImagePattern(ctx, x, y, width, height, 0, id, 1, image);
			}
		} else
			nvgImagePattern(ctx, x, y, width, height, 0, id, 1, image);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getImage() {
		return id;
	}

	public NVGPaint getNVGPaint() {
		return image;
	}

	public void cleanUp(final long ctx) {
		nvgDeleteImage(ctx, id);
		image.free();
	}

	public boolean isColor() {
		return false;
	}
}
