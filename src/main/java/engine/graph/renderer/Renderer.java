package engine.graph.renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import engine.graph.hlib.component.HWindow;
import engine.graph.renderer.mesh.Mesh;
import engine.graph.renderer.postprocess.PostProcessingRenderer;
import loader.generator.Dungeon;
import loader.generator.Salle;

public class Renderer {

    private DungeonRenderer dungeonRenderer;

    private PostProcessingRenderer postProcess;

    private ShaderProgram bg;

    private float angle;

    private float when;

    private boolean invertRotate = false;

    public static int instancedItem;

    public static int drawCalls;

    public static int instancedDrawCalls;

    public Renderer() {

    }

    public void init(final HWindow window) throws Exception {
        postProcess = new PostProcessingRenderer(window);

        bg = new ShaderProgram("bg");

        bg.createUniform("noiseTex");
        bg.createUniform("solTex");
        bg.createUniform("animAngle");

        bg.bind();
        bg.setInt("noiseTex", 0);
        bg.setInt("solTex", 1);
        bg.unbind();

        when = (float) Math.random() * 360;
    }

    public void setDungeonRenderer(final DungeonRenderer dungeon) {
        dungeonRenderer = dungeon;
    }

    public void render(final HWindow window, final Dungeon dungeon) {
        // Init rendering
        instancedItem = instancedDrawCalls = 0;
        glClear(GL_COLOR_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        postProcess.startRecord();

        // Render moving ground
        bg.bind();

        angle += invertRotate ? 1 : -1;
        if (!invertRotate && angle >= when) {
            when = (float) Math.random() * angle;
            invertRotate = true;
        } else if (invertRotate && angle <= when) {
            when = (float) Math.random() * 360 - 180;
            invertRotate = false;
        }

        bg.setFloat("animAngle", (float) Math.toRadians(angle));

        final Salle salle = dungeon.getActual();
        glActiveTexture(GL_TEXTURE0);
        salle.getNoise().bind();

        glActiveTexture(GL_TEXTURE1);
        salle.getSol().bind();

        Mesh.quad.render();
        bg.unbind();

        dungeonRenderer.render(window, dungeon);

        postProcess.stopRecord();
        postProcess.render();

        outPutError("Render map");
        /*
        System.out.println("instancedItem : " + instancedItem + ", in " + instancedDrawCalls + " draw calls");
         */
    }


    private void outPutError(final String message) {
        final int error = glGetError();
        if (error != GL_NO_ERROR)
            System.err.println(message + "GL error code : " + error);
    }

    public void cleanup() {
        dungeonRenderer.cleanup();
        bg.cleanup();
        postProcess.cleanup();
    }
}
