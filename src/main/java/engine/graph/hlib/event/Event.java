package engine.graph.hlib.event;

import org.lwjgl.glfw.GLFW;

public abstract class Event {

	private final int action;

	private final int mods;

	public Event(final int action, final int mods) {
		this.action = action;
		this.mods = mods;
	}

	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}

	public boolean isShiftPressed() {
		return getMods() == GLFW.GLFW_MOD_SHIFT;
	}

	public boolean isCtrlPressed() {
		return getMods() == GLFW.GLFW_MOD_CONTROL;
	}

	public boolean isAltPressed() {
		return getMods() == GLFW.GLFW_MOD_ALT;
	}

	public boolean isNumLockPressed() {
		return getMods() == GLFW.GLFW_MOD_NUM_LOCK;
	}

	public boolean isSuperPressed() {
		return getMods() == GLFW.GLFW_MOD_SUPER;
	}

	public boolean isPressed() {
		return getAction() == GLFW.GLFW_PRESS;
	}

	public boolean isReleassed() {
		return getAction() == GLFW.GLFW_RELEASE;
	}
}
