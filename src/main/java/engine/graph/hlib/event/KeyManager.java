package engine.graph.hlib.event;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFW;

public class KeyManager {

	private final static int KEYS = 349;

	private final boolean[] pressed;

	private final boolean[] releassed;

	public KeyManager() {
		pressed = new boolean[KEYS];
		releassed = new boolean[KEYS];
	}

	public void update(final long window) {	
		// Printable keys.
		for(int i = 32; i <= 96; i++)
			testKey(window, i);
		testKey(window, 161);
		testKey(window, 162);
		
		// Function keys.
		for(int i = 256; i <= 348; i++)
			testKey(window, i);
	}
	
	private void testKey(final long window, final int keyCode) {
		final int action = GLFW.glfwGetKey(window, keyCode);
		if (action == GLFW_PRESS) {
			pressed[keyCode] = true;
		} else if (action == GLFW_RELEASE && pressed[keyCode]) {
			pressed[keyCode] = false;
			releassed[keyCode] = true;
		}
	}
	
	public void reset() {
		for(int i = 0; i < KEYS; i++) {
			releassed[i] = false;
		}
	}

	public boolean isKeyPressed(final int key) {
		return pressed[key];
	}

	public boolean isKeyReleassed(final int key) {
		return releassed[key];
	}
}
