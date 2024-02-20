package engine.graph.hlib.graphics.paint;

import org.lwjgl.nanovg.NVGPaint;

public interface Paint2 extends Paint {

	void init(final long ctx, final int x, final int y);
	
	NVGPaint getNVGPaint();
}
