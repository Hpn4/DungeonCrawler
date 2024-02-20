package engine.graph.renderer.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import engine.graph.renderer.Renderer;
import engine.graph.renderer.mesh.AbstractMesh;
import org.lwjgl.system.MemoryUtil;

public class LayerMesh extends AbstractMesh {

	private static final int FLOAT_SIZE_BYTES = 4;

	// Position + texture offset
	protected static final int INSTANCE_SIZE_FLOATS = 3 + 2;

	private static final int INSTANCE_SIZE_BYTES = INSTANCE_SIZE_FLOATS * FLOAT_SIZE_BYTES;

	private final static float[] positions = { 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
			0.0f };

	private final static float[] textCoords = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f };

	private final static int[] indices = { 0, 1, 2, 0, 2, 3 };

	private final int numInstances;

	private final int instanceDataVBO;

	private FloatBuffer instanceDataBuffer;

	public LayerMesh(final int numInstances) {
		this.numInstances = numInstances;

		FloatBuffer posBuffer = null, textCoordsBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			vboIdList = new ArrayList<>();

			glBindVertexArray(vaoId = glGenVertexArrays());

			int start = 0;

			// ********************** //
			// **** Position VBO **** //
			// ********************** //
			int vboId = glGenBuffers();
			vboIdList.add(vboId);

			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions);
			posBuffer.flip();
			posBuffer.position(0);

			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(start);
			glVertexAttribPointer(start++, 3, GL_FLOAT, false, 0, 0);

			// ********************************* //
			// **** Texture Coordinates VBO **** //
			// ********************************* //
			vboIdList.add(vboId = glGenBuffers());

			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			textCoordsBuffer.put(textCoords).flip();

			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glEnableVertexAttribArray(start);
			glVertexAttribPointer(start++, 2, GL_FLOAT, false, 0, 0);

			// ******************* //
			// **** Index VBO **** //
			// ******************* //
			vboIdList.add(vboId = glGenBuffers());

			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			instanceDataVBO = glGenBuffers();
			vboIdList.add(instanceDataVBO);
			instanceDataBuffer = MemoryUtil.memAllocFloat(numInstances * INSTANCE_SIZE_FLOATS);
			glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);

			// ******************************** //
			// **** Instanced drawing data **** //
			// ******************************** //

			int strideStart = 0;

			// Position
			glVertexAttribPointer(start, 3, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(start, 1);
			glEnableVertexAttribArray(start++);

			strideStart += FLOAT_SIZE_BYTES * 3;

			// TexOffset
			glVertexAttribPointer(start, 2, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
			glVertexAttribDivisor(start, 1);
			glEnableVertexAttribArray(start);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);

		} finally {
			if (posBuffer != null)
				MemoryUtil.memFree(posBuffer);

			if (textCoordsBuffer != null)
				MemoryUtil.memFree(textCoordsBuffer);

			if (indicesBuffer != null)
				MemoryUtil.memFree(indicesBuffer);
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();

		if (instanceDataBuffer != null) {
			MemoryUtil.memFree(instanceDataBuffer);
			instanceDataBuffer = null;
		}
	}

	public int getNumInstances() {
		return numInstances;
	}

	public FloatBuffer getInstanceDataBuffer() {
		return instanceDataBuffer;
	}

	@Override
	public void render(final boolean withTexture) {
		initRender(true);

		glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
		glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_STATIC_DRAW);

		glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, numInstances);

		Renderer.instancedItem += numInstances;
		Renderer.instancedDrawCalls++;

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		endRender(true);
	}
}
