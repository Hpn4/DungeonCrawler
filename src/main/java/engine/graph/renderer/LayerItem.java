package engine.graph.renderer;

import java.nio.FloatBuffer;

import engine.graph.renderer.mesh.LayerMesh;
import loader.map.Layer;
import loader.map.Map;
import loader.map.Tile;
import loader.map.Tileset;

public class LayerItem {

    private LayerMesh mesh;

    private Tileset tileset;

    private Layer layer;

    public LayerItem(final Layer layer, final Map map) {
        this.layer = layer;

        change(layer, map);
    }

    public void change(final Layer layer, final Map map) {
        this.layer = layer;
        final Tileset ts = map.getTilest(layer.getAtlasId());
        final int cols = ts.getCols(), rows = ts.getRows();
        tileset = ts;

        // We count the number of non air tiles we have
        int numInstances = 0;
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++)
                if (layer.get(x, y) != Tile.AIR)
                    numInstances++;

        // We initialize our mesh and get the buffer object
        if (mesh != null)
            mesh.cleanup();
        mesh = new LayerMesh(numInstances);
        mesh.setTexture(ts.getTexture());

        final FloatBuffer f = mesh.getInstanceDataBuffer();

        // We register all the tiles of the layer in the buffer object of our mesh
        int buffPos = 0;
        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                final Tile t = layer.get(x, y);

                // If there is a tile, we insert it's position and it textures coord
                if (t != Tile.AIR) {
                    f.put(buffPos++, x);
                    f.put(buffPos++, y);
                    f.put(buffPos++, 0);

                    final int pos = t.getTile();
                    final int col = pos % cols, row = pos / cols;
                    f.put(buffPos++, (float) col / cols);
                    f.put(buffPos++, (float) row / rows);
                }
            }
    }

    public LayerMesh getMesh() {
        return mesh;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public Layer getLayer() {
        return layer;
    }

    public void cleanup() {
        mesh.cleanup();
        mesh = null;
    }
}
