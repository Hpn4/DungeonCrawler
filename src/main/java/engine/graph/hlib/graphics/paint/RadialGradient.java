package engine.graph.hlib.graphics.paint;

import static org.lwjgl.nanovg.NanoVG.nvgRadialGradient;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.nanovg.NVGPaint;

public class RadialGradient implements Paint2 {

	private final Vector2i center;

	private final Vector2f radius;

	private final Color startCol;

	private final Color endColor;

	private final NVGPaint gradient;

	public RadialGradient(final int x, final int y, final float innerRadius, final float outerRadius,
			final Color colStart, final Color colEnd) {
		center = new Vector2i(x, y);
		radius = new Vector2f(innerRadius, outerRadius);
		startCol = colStart;
		endColor = colEnd;
		gradient = NVGPaint.create();
	}

	public void init(final long ctx, final int x, final int y) {
		nvgRadialGradient(ctx, center.x, center.y, radius.x, radius.y, startCol.getColor(), endColor.getColor(),
				gradient);
	}

	public void cleanUp(long ctx) {
		startCol.cleanUp(ctx);
		endColor.cleanUp(ctx);
		gradient.free();
	}

	public boolean isColor() {
		return false;
	}

	public NVGPaint getNVGPaint() {
		return gradient;
	}

}
