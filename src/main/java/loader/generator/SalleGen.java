package loader.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.joml.Vector2i;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import engine.graph.hlib.graphics.paint.PerlinNoise;
import loader.map.Map;
import loader.map.TypeSalle;
import loader.texture.NoiseTexture;
import loader.texture.Texture;

public class SalleGen {

	/* Liste de nombres premiers > 9000 utilis� pour g�n�rer une texture de bruit. L'algorithme est plus rapide avec des nombres premiers */
	private final static int[] randomPrime = { 9001, 9007, 9011, 9013, 9029, 9041, 9043, 9049, 9059, 9067, 9091, 9103,
			9109, 9127, 9133, 9137, 9151, 9157, 9161, 9173, 9181, 9187, 9199, 9203, 9209, 9221, 9227, 9239, 9241, 9257,
			9277, 9281, 9283, 9293, 9311, 9319, 9323, 9337, 9341 };

	public static Salle[][] genereDungeon(final int minSalle, final int maxSalle, final int width, final int height)
			throws Exception {
		final Salle[][] salles = new Salle[height][width];

		final Stack<Vector2i> sallePos = new Stack<>();
		final Vector2i startPos = new Vector2i(width / 2, height / 2);
		sallePos.add(startPos);

		final int finalSalle = (int) (Math.random() * (maxSalle - minSalle)) + minSalle;

		int nmbSalle = 0;
		while (sallePos.size() > 0) {
			final Vector2i d = sallePos.pop();

			// La salle à déja été ajouté
			if (salles[d.y][d.x] != null)
				continue;

			final boolean canAdd = nmbSalle < finalSalle;

			// La salle à gauche
			int porteO = 2;
			if (d.x - 1 > -1) {
				if (salles[d.y][d.x - 1] == null)
					porteO = 0;
				else if (salles[d.y][d.x - 1].getMap().havePorteE())
					porteO = 1;
			}

			// La salle à droite
			int porteE = 2;
			if (d.x + 1 < width) {
				if (salles[d.y][d.x + 1] == null)
					porteE = 0;
				else if (salles[d.y][d.x + 1].getMap().havePorteO())
					porteE = 1;
			}

			// La salle en haut
			int porteN = 2;
			if (d.y - 1 > -1) {
				if (salles[d.y - 1][d.x] == null)
					porteN = 0;
				else if (salles[d.y - 1][d.x].getMap().havePorteS())
					porteN = 1;
			}

			// La salle en bas
			int porteS = 2;
			if (d.y + 1 < height) {
				if (salles[d.y + 1][d.x] == null)
					porteS = 0;
				else if (salles[d.y + 1][d.x].getMap().havePorteN())
					porteS = 1;
			}

			if (canAdd) {
				int nmbPorte = 0;
				if (porteN == 0)
					nmbPorte++;
				if (porteS == 0)
					nmbPorte++;
				if (porteO == 0)
					nmbPorte++;
				if (porteE == 0)
					nmbPorte++;

				nmbPorte = (int) (Math.random() * (nmbPorte - 1) + 1);
				int add = 0;

				while (add < nmbPorte) {
					if (Math.random() > 0.65f) {
						final float rand = (float) Math.random();
						if (porteN == 0 && rand <= 0.25f) {
							porteN = 1;
							add++;
						} else if (porteS == 0 && rand <= 0.5f) {
							porteS = 1;
							add++;
						} else if (porteO == 0 && rand <= 0.75f) {
							porteO = 1;
							add++;
						} else if (porteE == 0) {
							porteE = 1;
							add++;
						}
					}
				}
			} else {
				if (porteN == 0)
					porteN = 2;
				if (porteS == 0)
					porteS = 2;
				if (porteO == 0)
					porteO = 2;
				if (porteE == 0)
					porteE = 2;
			}

			final boolean mine = Math.random() < 0.1f;
			final Map s = new MapMaker(porteS, porteN, porteO, porteE, mine ? 1 : 0).getFinalMap();

			if (s.havePorteO() && d.x - 1 > -1)
				sallePos.add(new Vector2i(d.x - 1, d.y));

			if (s.havePorteE() && d.x + 1 < width)
				sallePos.add(new Vector2i(d.x + 1, d.y));

			if (s.havePorteN() && d.y - 1 > -1)
				sallePos.add(new Vector2i(d.x, d.y - 1));

			if (s.havePorteS() && d.y + 1 < height)
				sallePos.add(new Vector2i(d.x, d.y + 1));

			final Salle salle = new Salle(s);
			salle.setNoise(genNoiseTexture());
			salle.setSol(genGroundTexture());
			
			if(mine)
				salle.setTypeSalle(TypeSalle.MINE);
			nmbSalle++;

			salles[d.y][d.x] = salle;
		}

		return salles;
	}

	public static NoiseTexture genNoiseTexture() throws Exception {
		int index = (int) (Math.random() * (randomPrime.length - 1));
		final PerlinNoise perlin = new PerlinNoise(randomPrime[index], 8f, 0.8f, 1f, 1);
		final int w = 20, h = 10;

		try (final MemoryStack stack = MemoryStack.stackPush()) {
			final ByteBuffer img = stack.malloc(w * h);

			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++) {
					final double noise = perlin.getHeightO(x, y);
					img.put((byte) ((noise + 1) * 75f));
				}

			return new NoiseTexture(w, h, img.position(0));
		}
	}

	public static Texture genGroundTexture() throws Exception {
		// On prends une image de l'herbe aleatoire
		int random = (int) (Math.random() * 5);
		final BufferedImage grass = ImageIO.read(new File("resources/sol/grass/" + random + ".png"));
		
		// On prend une image de terre aleatoire
		random = (int) (Math.random() * 5);
		final BufferedImage dirt = ImageIO.read(new File("resources/sol/dirt/" + random + ".png"));

		// width * height * canaux (RGBA)
		final int w = grass.getWidth(), h = grass.getHeight();
		final ByteBuffer img = MemoryUtil.memAlloc(w * h * 4);
		int index = 0;
		
		// On combine les deux images en une seules
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++) {
				final int rgbG = grass.getRGB(x, y);
				final int alpha = (rgbG >> 24) & 0xff;

				// Il n'y a pas d'herbe
				if (alpha == 0) {
					final int rgbD = dirt.getRGB(x, y);

					img.put(index++, (byte) ((rgbD >> 16) & 0xff));
					img.put(index++, (byte) ((rgbD >> 8) & 0xff));
					img.put(index++, (byte) ((rgbD >> 0) & 0xff));
					img.put(index++, (byte) ((rgbD >> 24) & 0xff));
				} else {
					img.put(index++, (byte) ((rgbG >> 16) & 0xff));
					img.put(index++, (byte) ((rgbG >> 8) & 0xff));
					img.put(index++, (byte) ((rgbG >> 0) & 0xff));
					img.put(index++, (byte) alpha);
				}
			}

		final Texture ground = new Texture(w, h, img.position(0));

		MemoryUtil.memFree(img);

		return ground;
	}
}