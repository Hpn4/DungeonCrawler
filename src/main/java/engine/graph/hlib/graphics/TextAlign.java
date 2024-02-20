package engine.graph.hlib.graphics;

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_BASELINE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_BOTTOM;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

public enum TextAlign {

	BASELINE(NVG_ALIGN_BASELINE), BOTTOM(NVG_ALIGN_BOTTOM), CENTER(NVG_ALIGN_CENTER), LEFT(NVG_ALIGN_LEFT),
	MIDDLE(NVG_ALIGN_MIDDLE), RIGHT(NVG_ALIGN_RIGHT), TOP(NVG_ALIGN_TOP);

	private final int align;

	TextAlign(final int align) {
		this.align = align;
	}

	public int getAlign() {
		return align;
	}
}
