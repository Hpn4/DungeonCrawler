package engine.graph.hlib.border;

import engine.graph.hlib.graphics.paint.Color;
import engine.graph.hlib.graphics.paint.Paint;

public class Borders {

	public static Border raised(final Color col) {
		return new BevelBorder(BevelBorder.RAISED, col);
	}

	public static Border raised(final Color highlight, final Color shadow) {
		return new BevelBorder(BevelBorder.RAISED, highlight, shadow);
	}

	public static Border raised(final Color highlightOuterColor, final Color highlightInnerColor,
			final Color shadowOuterColor, final Color shadowInnerColor) {
		return new BevelBorder(BevelBorder.RAISED, highlightOuterColor, highlightInnerColor, shadowOuterColor,
				shadowInnerColor);
	}

	public static Border lowered(final Color col) {
		return new BevelBorder(BevelBorder.LOWERED, col);
	}

	public static Border lowered(final Color highlight, final Color shadow) {
		return new BevelBorder(BevelBorder.LOWERED, highlight, shadow);
	}

	public static Border lowered(final Color highlightOuterColor, final Color highlightInnerColor,
			final Color shadowOuterColor, final Color shadowInnerColor) {
		return new BevelBorder(BevelBorder.LOWERED, highlightOuterColor, highlightInnerColor, shadowOuterColor,
				shadowInnerColor);
	}

	public static Border compound(final Border outsideBorder, final Border insideBorder) {
		return new CompoundBorder(outsideBorder, insideBorder);
	}

	public static Border empty() {
		return new EmptyBorder();
	}

	public static Border raisedEtched(final Color color) {
		return new EtchedBorder(EtchedBorder.RAISED, color);
	}

	public static Border raisedEtched(final Color highlight, final Color shadow) {
		return new EtchedBorder(EtchedBorder.RAISED, highlight, shadow);
	}

	public static Border loweredEtched(final Color color) {
		return new EtchedBorder(EtchedBorder.LOWERED, color);
	}

	public static Border loweredEtched(final Color highlight, final Color shadow) {
		return new EtchedBorder(EtchedBorder.LOWERED, highlight, shadow);
	}

	public static Border line(final Paint paint) {
		return new LineBorder(paint);
	}

	public static Border line(final Paint paint, final int width) {
		return new LineBorder(paint, width);
	}
}
