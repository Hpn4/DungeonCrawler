package engine.graph.hlib.component;

import engine.graph.hlib.graphics.Graphics;

public class Box {

	public static Filler getVerticalStruct(final int height) {
		return new Filler(0, height);
	}

	public static Filler getHorizontalStruct(final int width) {
		return new Filler(width, 0);
	}

	public static class Filler extends HComponent {

		public Filler(final int x, final int y) {
			setSize(x, y);
		}

		@Override
		protected void paintComponent(final Graphics g) {
		}

		@Override
		protected void initSize(final Graphics g) {
		}

	}
}
