package engine.graph.renderer.postprocess;

import engine.graph.renderer.mesh.Mesh;
import engine.graph.renderer.ShaderProgram;

/**
 * 
 * Cette objet envoie la texture du frame buffer précédent et l'affiche
 * direcement a l'ecran. Il effectue des modifications supplémentaires grace aux
 * shaders "toScreen.vert" et "toScreen.frag"
 * 
 * @author Hpn4
 *
 */
public class ToScreenStep {

	private final ShaderProgram toScreenShaderProgram;

	public ToScreenStep() throws Exception {
		toScreenShaderProgram = new ShaderProgram("toScreen");

		toScreenShaderProgram.createUniform("sceneTex");

		toScreenShaderProgram.bind();

		toScreenShaderProgram.setInt("sceneTex", 0);

		toScreenShaderProgram.unbind();
	}

	public void render() {
		toScreenShaderProgram.bind();

		Mesh.quad.render();

		toScreenShaderProgram.unbind();
	}

	public void cleanup() {
		toScreenShaderProgram.cleanup();
	}
}
