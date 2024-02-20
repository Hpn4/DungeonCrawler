package engine.graph.renderer.postprocess;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import java.nio.ByteBuffer;

import org.lwjgl.system.MemoryStack;

import engine.graph.hlib.component.HWindow;

public class ToFBOBuffer {

	/**
	 * 2 textures :
	 * 
	 * 0 : the default scene, the output of the "light" shader 1 : the emissive
	 * texture to be passed to the bloom downscale shader
	 */
	private final static int TOTAL_TEXTURES = 2;

	private final int screenFBO;

	private final int[] ids;

	private final int width;

	private final int height;

	public ToFBOBuffer(final HWindow window) throws Exception {
		this.width = window.getWidth();
		this.height = window.getHeight();

		screenFBO = glGenFramebuffers();

		// Bind
		glBindFramebuffer(GL_FRAMEBUFFER, screenFBO);

		ids = new int[TOTAL_TEXTURES];
		glGenTextures(ids);

		// Create textures for depth, diffuse / emissive color, specular color,
		// normal and
		// shadow factor/ reflectance / isEmissive
		for (int i = 0; i < TOTAL_TEXTURES; i++) {
			glBindTexture(GL_TEXTURE_2D, ids[i]);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_FLOAT, (ByteBuffer) null);
			// For sampling
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			/**
			 * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			 * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			 * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_NONE);
			 * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			 * glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			 */

			// Attach the the texture to the G-Buffer
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, ids[i], 0);
		}

		try (final MemoryStack stack = MemoryStack.stackPush()) {
			glDrawBuffers(stack.ints(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1));
		}

		// On vérifie que tous est bon
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			throw new Exception("Could not create FrameBuffer");

		// Unbind
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getIds() {
		return ids;
	}

	public int getSceneTexture() {
		return ids[0];
	}

	public int getEmissiveTexture() {
		return ids[1];
	}

	public int getScreenFBO() {
		return screenFBO;
	}

	public void bindFrameBuffer() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, screenFBO);
	}

	public void cleanup() {
		glDeleteFramebuffers(screenFBO);
		glDeleteTextures(ids);
	}
}