package engine.graph.renderer.mesh;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.util.List;

import loader.texture.Texture;

public abstract class AbstractMesh {

    protected int vaoId;

    /**
     * Array of all VBO indices (postions, normals, indices, textures...)
     */
    protected List<Integer> vboIdList;

    protected Texture texture;

    public void setTexture(final Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    protected void initRender(final boolean withTexture) {
        if (withTexture && texture != null) {
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
    }

    public void render() {
        render(true);
    }

    public abstract void render(final boolean withTexture);

    protected void endRender(final boolean withTexture) {
        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        if (withTexture) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public void cleanup() {
        glBindVertexArray(getVaoId());
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList)
            glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public int getVaoId() {
        return vaoId;
    }
}
