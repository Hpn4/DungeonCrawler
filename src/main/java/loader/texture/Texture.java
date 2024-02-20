package loader.texture;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import loader.map.Utils;

/**
 * Class stockant en memoire l'image préciser dans le constructeur. Cette class
 * contient egalement l'id (le pointeur de l'image), et ses dimensions.
 * 
 * @author Hpn4
 *
 */
public class Texture extends AbstractTexture {

	private final int width;

	private final int height;

	protected Texture(final String fileName) throws Exception {
		super();
		ByteBuffer buf;

		// Load Texture file
		try (final MemoryStack stack = MemoryStack.stackPush()) {
			final IntBuffer w = stack.mallocInt(1), h = stack.mallocInt(1), channels = stack.mallocInt(1);

			buf = stbi_load(Utils.path(fileName), w, h, channels, 4);

			if (buf == null)
				throw new Exception("Image file [" + Utils.path(fileName) + "] not loaded: " + stbi_failure_reason());

			width = w.get();
			height = h.get();
		}

		createTexture(buf);

		stbi_image_free(buf);
	}

	public Texture(final int width, final int height, final ByteBuffer image) throws Exception {
		super();

		this.width = width;
		this.height = height;

		createTexture(image);
	}

	private void createTexture(final ByteBuffer buf) {
		// Bind the texture
		bind();

		// Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	}

	/**
	 * NE PAS OUBLIER DE LIBERER LE BUFFER MemoryUtil.memFree(buff)
	 * 
	 * @return
	 */
	public ByteBuffer getImage() {
		final ByteBuffer px = MemoryUtil.memAlloc(width * height * 4);

		bind();
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, px);
		return px;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
}
