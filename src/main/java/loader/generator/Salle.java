package loader.generator;

import java.util.ArrayList;

import engine.graph.hlib.component.HWindow;
import game.entity.mob.Mob;
import loader.map.Map;
import loader.map.TypeSalle;
import loader.texture.NoiseTexture;
import loader.texture.Texture;

public class Salle {

	// La texture de bruit pour le pseudo "feuillage" vert qui se déplace
	private NoiseTexture noiseTex;

	// La texture du sol composé d'herbe et de terre
	private Texture sol;

	// L'objet map contenant toute les tiles et layer à afficher
	private Map map;

	/* Si les murs de la salle sont activé */
	private boolean murPorte;

	/* Si la salle à déja été visité */
	private boolean visited;

	/* Les mobs présent dans la salle */
	private final ArrayList<Mob> mobs;
	
	/* Precise le type de la salle. Si c'est une mine, un coffre... */
	private TypeSalle type;

	public Salle(final Map map) {
		this.map = map;
		mobs = new ArrayList<>();
		visited = false;
	}

	/**
	 * Cette méthode sera appellé lorsque le joueur entre dans la salle pour la
	 * première fois. Elle sert à charger les monstres de la salle.
	 * 
	 * @param window
	 */
	public void init(final HWindow window) {

	}

	public void setMurPorte(final boolean murPorte) {
		this.murPorte = murPorte;
		map.getLayers().get(2).setVisible(murPorte);
	}

	public boolean isMurSet() {
		return murPorte;
	}

	public void setVisited(final boolean visit) {
		this.visited = visit;
	}

	public boolean isVisited() {
		return visited;
	}

	public int getTileId(final int x, final int y, final int layer) {
		return map.getLayers().get(layer).get(x, y).getTile();
	}

	public int getTileId(final int x, final int y, final String layer) {
		return map.getLayer(layer).get(x, y).getTile();
	}

	public Map getMap() {
		return map;
	}

	public void setNoise(final NoiseTexture tex) {
		noiseTex = tex;
	}

	public NoiseTexture getNoise() {
		return noiseTex;
	}

	public void setSol(final Texture tex) {
		sol = tex;
	}

	public Texture getSol() {
		return sol;
	}
	
	public void setTypeSalle(final TypeSalle typeSalle) {
		type = typeSalle;
	}
	
	public TypeSalle getTypeSalle() {
		return type;
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public void cleanup() {
		noiseTex.cleanup();
		sol.cleanup();

		// On clean tout les mobs
		for (final Mob mob : mobs)
			mob.cleanup();
	}
}
