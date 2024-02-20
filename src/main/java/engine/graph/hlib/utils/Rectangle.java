package engine.graph.hlib.utils;

public class Rectangle {

	public int x;

	public int y;

	public int width;

	public int height;

	public Rectangle(final int x, final int y, final int width, final int height) {
		set(x, y, width, height);
	}
	
	public Rectangle(final Rectangle source) {
		set(source);
	}

	public void set(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void set(final Rectangle source) {
		set(source.x, source.y, source.width, source.height);
	}

	public String toString() {
		return "x:" + x + ", y:" + y + ", width:" + width + ", height:" + height;
	}
}
