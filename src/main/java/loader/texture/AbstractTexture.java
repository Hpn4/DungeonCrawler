package loader.texture;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;

public abstract class AbstractTexture {

	protected final int id;

	public AbstractTexture() {
		id = glGenTextures();
	}

	public abstract void bind();

	public int getId() {
		return id;
	}

	public void cleanup() {
		glDeleteTextures(id);
	}
}
