package loader.map;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2f;

public class Map {

	private final int width;

	private final int height;

	private final ArrayList<Tileset> tileset;

	private final ArrayList<AbstractLayer> layers;

	private boolean porteN;

	private boolean porteS;

	private boolean porteO;

	private boolean porteE;

	public Map(final String fileMap) throws Exception {
		String contenue = Utils.readAbs(fileMap);

		HashMap<String, Object> attrib = getBaliseAttribute(contenue, "map");

		tileset = new ArrayList<>();
		layers = new ArrayList<>();

		width = (int) attrib.get("width");
		height = (int) attrib.get("height");

		// On stock toute les tiles
		int index = 0;
		int size = "<tileset".length();
		while ((index = contenue.indexOf("<tileset")) != -1) {
			attrib = getBaliseAttribute(contenue, "tileset");

			// On avance dans le lecture du fichier
			contenue = contenue.substring(index + size);

			final Tileset t = new Tileset(fileMap, attrib.get("source").toString(), (int) attrib.get("firstgid"));
			tileset.add(t);
		}

		index = 0;
		size = tileset.size();

		// On parcours toute les couches
		while ((index = contenue.indexOf("<layer")) != -1) {

			AbstractLayer layer;
			boolean set = false;

			// On avance dans le lecture du fichier
			contenue = contenue.substring(index);

			index = contenue.indexOf("name");
			String name = "";
			if (index != -1) {
				name = contenue.substring(index + "name=\"".length());
				name = name.substring(0, name.indexOf("\""));
			}

			// On genere le layer
			if (name.equals("special"))
				layer = new ZLayer(name);
			else
				layer = new Layer(width, height, name);

			// Balise layer
			contenue = contenue.substring(contenue.indexOf(">") + 2);

			// Balise data
			contenue = contenue.substring(contenue.indexOf(">") + 2);

			layers.add(layer);

			// On extrait la liste de nombre et on enlève les sauts de ligne
			String sousPart = contenue.substring(0, contenue.indexOf("<"));
			sousPart = sousPart.replace("\n", "");

			final String[] tilesId = sousPart.split(",");
			int x = 0, y = 0;
			for (int i = 0; i < tilesId.length; i++) {
				index = Integer.parseInt(tilesId[i]);

				if (index == 0)
					layer.set(x, y, Tile.AIR);
				else {
					// On recupère l'id de l'atlas en fonction du numéro
					int atlasId = 0;
					for (int j = 0; j < tileset.size(); j++) {
						final Tileset ts = tileset.get(j);
						final int grid = ts.getFirstGid();
						if (index >= grid && index < grid + ts.getTileCount()) {
							atlasId = j;
							break;
						}
					}

					final Tile t = new Tile(index - tileset.get(atlasId).getFirstGid(), atlasId);
					layer.set(x, y, t);
					if (!set) {
						set = true;
						layer.setAtlasId((short) atlasId);
						if (layer instanceof ZLayer)
							((ZLayer) layer).setTileset(tileset.get(atlasId));
					}
				}

				// On incremente la position
				x++;
				if (x >= width) {
					y++;
					x = 0;
				}
			}
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

	public Vector2f getTexCoord(final Tile tile) {
		return tileset.get(tile.getAtlasId()).getTextCoord(tile.getTile());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setPorte(final boolean porteN, final boolean porteS, final boolean porteO, final boolean porteE) {
		this.porteN = porteN;
		this.porteS = porteS;
		this.porteO = porteO;
		this.porteE = porteE;
	}

	public void setPorteN(final boolean porteN) {
		this.porteN = porteN;
	}

	public void setPorteS(final boolean porteS) {
		this.porteS = porteS;
	}

	public void setPorteO(final boolean porteO) {
		this.porteO = porteO;
	}

	public void setPorteE(final boolean porteE) {
		this.porteE = porteE;
	}

	public boolean havePorteN() {
		return porteN;
	}

	public boolean havePorteS() {
		return porteS;
	}

	public boolean havePorteO() {
		return porteO;
	}

	public boolean havePorteE() {
		return porteE;
	}

	public int getLayersCount() {
		return layers.size();
	}

	public ArrayList<AbstractLayer> getLayers() {
		return layers;
	}

	public AbstractLayer getLayer(final String name) {
		for (final AbstractLayer layer : layers)
			if (layer.getName().equals(name))
				return layer;

		return null;
	}

	public ArrayList<Tileset> getTilesets() {
		return tileset;
	}

	private int getTileset(final String path) {
		for (int i = 0; i < tileset.size(); i++)
			if (path.equals(tileset.get(i).getImagePath()))
				return i;

		return -1;
	}

	public void add(final ArrayList<Tileset> tilesets, final ArrayList<AbstractLayer> layer) {
		for (int i = 0; i < tilesets.size(); i++) {
			final Tileset ts = tilesets.get(i);
			final int index = getTileset(ts.getImagePath());
			final int id;

			// Si le tileset n'est pas dans cette carte, on l'ajoute
			if (index == -1) {
				id = tileset.size();
				tileset.add(tilesets.get(i));
			} else
				id = index;

			// Une fois qu'on à l'ID, on met à jour les id du layer
			for (int j = 0; j < layer.size(); j++) {
				final AbstractLayer l = layer.get(j);
				if (l.getAtlasId() == i) {
					l.forEach(e -> e.setAtlasId(id));
					l.setAtlasId((short) id);
					layers.add(l);
					layer.remove(j);
				}
			}
		}

		for (final AbstractLayer l : layer) {
			final Tileset ts = tilesets.get(l.getAtlasId());
			final int index = getTileset(ts.getImagePath());

			l.forEach(e -> e.setAtlasId(index));
			l.setAtlasId((short) index);
		}

		layers.addAll(layer);

		// for(final AbstractLayer l : layers)
		// System.out.println(l.getName() + " " +
		// tileset.get(l.getAtlasId()).getImagePath());
	}

	public Tileset getTilest(final int id) {
		return tileset.get(id);
	}
}
