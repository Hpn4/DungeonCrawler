package loader.map;

import java.util.HashMap;

import org.joml.Vector2f;

import loader.texture.Texture;
import loader.texture.TextureCache;

public class Tileset {

    private final static int TILE_SIZE = 32;

    private final int firstGid;

    private final int tileCount;

    private final int cols;

    private final int rows;

    private final String imagePath;

    private final Vector2f[] vecs;

    private Texture texture;

    public Tileset(final String parentPath, final String path, final int firstGid) throws Exception {
        final String firstPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
        final String content = Utils.readAbs(firstPath + "/" + path);

        this.firstGid = firstGid;

        final HashMap<String, Object> att = getBaliseAttribute(content, "image");
        final int width = (int) att.get("width"), height = (int) att.get("height");

        imagePath = att.get("source").toString();

        rows = height / TILE_SIZE;
        cols = width / TILE_SIZE;
        vecs = new Vector2f[tileCount = rows * cols];

        // Les coordonnées de texture
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                final int pos = i * cols + rows;
                final int col = pos % cols;
                final int row = pos / cols;
                vecs[i * cols + j] = new Vector2f((float) col / cols, (float) row / rows);
            }
    }

    public HashMap<String, Object> getBaliseAttribute(final String fileMap, final String balise) {
        int index = fileMap.indexOf("<" + balise);

        String firstSplit = fileMap.substring(index + balise.length() + 2);

        firstSplit = firstSplit.substring(0, firstSplit.indexOf('>') - (firstSplit.contains("/>") ? 2 : 1));

        final String parts[] = firstSplit.split("\" ");
        final HashMap<String, Object> attributes = new HashMap<>();
        for (final String part : parts) {
            index = part.indexOf("=");
            final String key = part.substring(0, index), value = part.substring(index + 2);

            try {
                // On test si c'est un entier
                attributes.put(key, Integer.parseInt(value));
            } catch (final NumberFormatException e) {

                try {
                    // On test si c'est un float
                    attributes.put(key, Float.parseFloat(value));
                } catch (final NumberFormatException e1) {
                    // Sinon on l'ajoute en tant que chaine de caractère
                    attributes.put(key, value);
                }
            }
        }

        return attributes;
    }

    public void init() throws Exception {
        texture = (Texture) TextureCache.getTexture(imagePath);
    }

    public int getFirstGid() {
        return firstGid;
    }

    public int getTileCount() {
        return tileCount;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Vector2f getTextCoord(final int idTile) {
        return vecs[idTile];
    }

    public Texture getTexture() {
        return texture;
    }
}
