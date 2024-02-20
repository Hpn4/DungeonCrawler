package engine.graph.hlib.event;

import org.lwjgl.glfw.GLFW;

public class KeyEvent extends Event {

	public static int action, mods, key;
	
	private final char txt;

	public KeyEvent(final char txt) {
		super(action, mods);
		this.txt = txt;
	}

	public int getKeyCode() {
		return key;
	}

	public char getText() {
		return txt;
	}

	public boolean isRepeated() {
		return getAction() == GLFW.GLFW_REPEAT;
	}
}
