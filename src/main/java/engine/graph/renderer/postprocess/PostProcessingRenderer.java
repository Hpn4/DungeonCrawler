package engine.graph.renderer.postprocess;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import engine.graph.hlib.component.HWindow;
import engine.graph.renderer.mesh.Mesh;

public class PostProcessingRenderer {

	private final ToFBOStep toFBO;
	
	private final ToScreenStep toScreen;
	
	public PostProcessingRenderer(final HWindow window) throws Exception {
		toFBO = new ToFBOStep(window);
		toScreen = new ToScreenStep();
	}
	
	public void render() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, toFBO.getBuffer().getSceneTexture());
		
		toScreen.render();
	}
	
	public void startRecord() {
		toFBO.startRender();
	}
	
	public void stopRecord() {
		toFBO.endRender();
	}
	
	public void cleanup() {
		toFBO.cleanup();
		toScreen.cleanup();
		Mesh.quad.cleanup();
	}
	
	public ToFBOStep getFBO() {
		return toFBO;
	}
}
