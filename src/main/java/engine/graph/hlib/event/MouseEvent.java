package engine.graph.hlib.event;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import engine.graph.hlib.component.HWindow;

public class MouseEvent extends Event {

	private static Vector2i tmpMousePos = new Vector2i();

	private static int tmpButton;

	private final Vector2i mousePos;

	private final Vector2i mouseOffset;

	private final int button;

	private boolean scroll;

	public final static int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT, BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT,
			BUTTON_MIDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

	public MouseEvent(final int button, final int action, final int mods) {
		super(action, mods);
		tmpButton = this.button = button;
		mousePos = new Vector2i(tmpMousePos);
		mouseOffset = new Vector2i();
	}

	public MouseEvent(final double xPos, final double yPos) {
		this(xPos, yPos, false);
	}

	public MouseEvent(final double x, final double y, final boolean scroll) {
		super(0, 0);
		if (scroll) {
			this.scroll = scroll;
			mouseOffset = new Vector2i((int) Math.round(x), (int) Math.round(y));
		} else {
			final double ratio = (double) HWindow.frameWidth / HWindow.initWidth;
			tmpMousePos.x = (int) Math.round((double) x * ratio);
			tmpMousePos.y = (int) Math.round((double) y * ratio);
			mouseOffset = new Vector2i();
		}

		button = tmpButton;
		mousePos = new Vector2i(tmpMousePos);
	}

	public void add(final int x, final int y) {
		mousePos.add(x, y);
	}

	public int getX() {
		return mousePos.x;
	}

	public int getY() {
		return mousePos.y;
	}

	public Vector2i getMousePos() {
		return mousePos;
	}

	public int getOffX() {
		return mouseOffset.x;
	}

	public int getOffY() {
		return mouseOffset.y;
	}

	public Vector2i getMouseOffset() {
		return mouseOffset;
	}

	public int getButton() {
		return button;
	}

	public boolean isScrolling() {
		return scroll;
	}

	public boolean isButton(final int but) {
		return getButton() == but;
	}
}
