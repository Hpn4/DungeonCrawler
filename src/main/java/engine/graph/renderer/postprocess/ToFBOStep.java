package engine.graph.renderer.postprocess;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import engine.graph.hlib.component.HWindow;

public class ToFBOStep {

	private final ToFBOBuffer buffer;

	public ToFBOStep(final HWindow window) throws Exception {
		buffer = new ToFBOBuffer(window);
	}

	public void startRender() {
		buffer.bindFrameBuffer();
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public void endRender() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}

	public ToFBOBuffer getBuffer() {
		return buffer;
	}

	public void cleanup() {
		buffer.cleanup();
	}
}
