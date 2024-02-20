package engine.graph.renderer;

import java.util.ArrayList;

import org.joml.Matrix4f;

import engine.graph.hlib.component.HWindow;
import loader.generator.Salle;
import loader.map.AbstractLayer;
import loader.map.Layer;
import loader.map.Map;
import loader.map.ZLayer;

public class MapRenderer {

    private final ArrayList<LayerItem> layers;

    private final ArrayList<ZLayer> zLayers;

    private final ShaderProgram program;

    private final ShaderProgram programZ;

    public MapRenderer(final HWindow window) throws Exception {
        layers = new ArrayList<>();
        zLayers = new ArrayList<>();

        // Normal shaders (for regular layers)
        program = new ShaderProgram("dungeon");

        program.createUniform("matrix");
        program.createUniform("numCols");
        program.createUniform("numRows");
        program.createUniform("atlas");

        // Z shaders (for layers with depth)
        programZ = new ShaderProgram("dungeonZ");

        programZ.createUniform("matrix");
        programZ.createUniform("numCols");
        programZ.createUniform("numRows");
        programZ.createUniform("tilePos");
        programZ.createUniform("texOff");
        programZ.createUniform("atlas");

        // Set the ortho matrix
        final Matrix4f ortho = new Matrix4f(window.getOrthoMatrix());
        program.bind();
        program.setMatrix4f("matrix", ortho);
        program.setInt("atlas", 0);
        program.unbind();

        programZ.bind();
        programZ.setMatrix4f("matrix", ortho);
        programZ.setInt("atlas", 0);
        programZ.unbind();
    }

    public void setSalle(final Salle salle) throws Exception {
        final Map map = salle.getMap();
        zLayers.clear();

        for (int i = map.getLayersCount(); i < layers.size(); ) {
            layers.get(i).cleanup();
            layers.remove(i);
        }

        // On parcourt tout les layers
        for (int i = 0; i < map.getLayersCount(); i++) {
            final AbstractLayer layer = map.getLayers().get(i);

            // Si c'est une couche simple, on la dessine en instanced rendering
            if (layer instanceof Layer) {
                if (layers.size() <= i)
                    layers.add(new LayerItem((Layer) layer, map));
                else
                    layers.get(i).change((Layer) layer, map);
            } else {
                // Sinon, on ajoute dans une autre liste et on charge la texture
                final ZLayer zLayer = (ZLayer) layer;
                zLayer.getTileset().init();
                zLayers.add(zLayer);
            }
        }

        for (LayerItem layer : layers) {
            final String name = layer.getLayer().getName();
            final boolean visible = name.equals("porte") || name.equals("collision");
            layer.getLayer().setVisible(!visible);
            layer.getTileset().init();
            layer.getMesh().setTexture(layer.getTileset().getTexture());
        }

        for (ZLayer zLayer : zLayers) zLayer.getTileset().init();
    }

    public ArrayList<ZLayer> getZLayers() {
        return zLayers;
    }

    public ShaderProgram getShader() {
        return programZ;
    }

    public void render() {
        program.bind();

        for (final LayerItem item : layers)
            if (item.getLayer().isVisible()) {
                program.setInt("numCols", item.getTileset().getCols());
                program.setInt("numRows", item.getTileset().getRows());

                item.getMesh().render();
            }

        program.unbind();
    }

    public void cleanup() {
        for (final LayerItem item : layers)
            item.cleanup();

        layers.clear();
        zLayers.clear();
        program.cleanup();
        programZ.cleanup();
    }
}
