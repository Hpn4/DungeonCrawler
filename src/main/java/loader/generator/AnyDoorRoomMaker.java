package loader.generator;

import java.io.File;
import java.util.ArrayList;

import loader.map.Map;

public class AnyDoorRoomMaker extends AbstractRoomMaker {

	private final static String path = "templates/pattern/any/";

	private final static ArrayList<String> angle;

	private final static ArrayList<String> angleO;

	private final static ArrayList<String> angleL;

	private final static ArrayList<String> angleLO;

	private final static ArrayList<String> centre;

	private final static ArrayList<String> centreS;

	private final static ArrayList<String> croix;

	// On récupère tous les patterns différents
	static {
		angle = new ArrayList<>();
		angleO = new ArrayList<>();
		angleL = new ArrayList<>();
		angleLO = new ArrayList<>();
		centre = new ArrayList<>();
		centreS = new ArrayList<>();
		croix = new ArrayList<>();

		final String[] files = new File(path).list();
		for (final String file : files) {
			// Les angles normaux et opposées petits
			if (file.startsWith("angle_") && file.endsWith("_1.tmx") && !file.equals("angle_1.tmx"))
				angleO.add(file);
			else if (file.startsWith("angle_"))
				angle.add(file);

			// Mes angles normaux et oposées grand
			else if (file.startsWith("angleL_") && file.endsWith("_1.tmx") && !file.equals("angleL_1.tmx"))
				angleLO.add(file);
			else if (file.startsWith("angleL_"))
				angleL.add(file);

			// Les centre petits et grands
			else if (file.startsWith("centre_"))
				centre.add(file);
			else if (file.startsWith("centreS_"))
				centreS.add(file);

			// La croix (4 chemin)
			else if (file.startsWith("croix_"))
				croix.add(file);
		}
	}

	// 0 : rien, 1 : petit, 2 : gros
	private int centreFlag;

	private int croixFlag;

	private int angleFlag;

	private int angleOFlag;

	public AnyDoorRoomMaker() throws Exception {
		float random = (float) Math.random();

		// 10% de chance d'avoir un gros centre
		if (random <= 0.1f) {
			map = getRandomFrom(centre);
			centreFlag = 2;
		}

		// 10% de chance d'avoir un petit centre
		else if (random <= 0.2f) {
			map = getRandomFrom(centreS);
			centreFlag = 1;
		}

		// 20% de chance d'avoir une croix
		else if (random <= 0.4f) {
			map = getRandomFrom(croix);
			croixFlag = 1;
		}

		// 30% de chance d'avoir un petit angle
		else if (random <= 0.7f) {
			// On recup un angle aleatoire parmi la liste des petits angles
			final int index = Math.round((float) Math.random() * (angle.size() - 1));

			// 50% de chance de commencer avec un angle oppose
			final boolean isAngleO = Math.random() <= 0.5f;

			// Si on a choisit de commencer avec un angle oppos�, on en prend un, sinon on
			// prend un angle normal
			if (isAngleO) {
				map = new Map(path + angleO.get(index));
				angleOFlag = 1;
			} else {
				map = new Map(path + angle.get(index));
				angleFlag = 1;
			}

			random = (float) Math.random();
			boolean petit = true;

			// 35% de chance de prendre son opposée
			if (random <= 0.35f)
				if (isAngleO) {
					combine(new Map(path + angle.get(index)));
					angleFlag = 1;
				} else {
					combine(new Map(path + angleO.get(index)));
					angleOFlag = 1;
				}

			// 25% de chance de prendre un autre angle opposées de même cat�gorie
			else if (random <= 0.6f)
				if (isAngleO) {
					combine(getRandomFrom(angle));
					angleFlag = 1;
				} else {
					combine(getRandomFrom(angleO));
					angleOFlag = 1;
				}

			// 20% de chance de prendre un angle opposée d'un autre type
			else if (random <= 0.8f) {
				if (isAngleO) {
					combine(getRandomFrom(angleL));
					angleFlag = 2;
				} else {
					combine(getRandomFrom(angleLO));
					angleOFlag = 2;
				}
				petit = false;
			}

			// 20% de chance de n'avoir qu'un seul angle
			random = (float) Math.random();

			// Si que petit angle
			if (petit) {

				// 25% de chance de prendre un petit centre
				if (random <= 0.25f) {
					combine(getRandomFrom(centreS));
					centreFlag = 1;
				}

				// 30% de chance grand centre
				else if (random <= 0.55f) {
					combine(getRandomFrom(centre));
					centreFlag = 2;
				}

				// 45% de chance pas de centre
			}
			// Si un grand angle, 40% de chance petit centre
			else if (random <= 0.40f) {
				combine(getRandomFrom(centreS));
				centreFlag = 1;
			}
		}

		// 20% de chance d'avoir un grand angle
		else if (random <= 0.90f) {
			final int index = Math.round((float) Math.random() * (angleL.size() - 1));

			final boolean isAngleO = Math.random() <= 0.5f;

			if (isAngleO) {
				map = new Map(path + angleLO.get(index));
				angleOFlag = 2;
			} else {
				map = new Map(path + angleL.get(index));
				angleFlag = 2;
			}

			random = (float) Math.random();

			// 40% de chance de prendre son opposée
			if (random <= 0.4f)
				if (isAngleO) {
					combine(new Map(path + angleL.get(index)));
					angleFlag = 2;
				} else {
					combine(new Map(path + angleLO.get(index)));
					angleOFlag = 2;
				}

			// 25% de chance de prendre un autre angle opposées de même type
			else if (random <= 0.65f)
				if (isAngleO) {
					combine(getRandomFrom(angleL));
					angleFlag = 2;
				} else {
					combine(getRandomFrom(angleLO));
					angleOFlag = 2;
				}

			// 10% de chance de prendre un angle opposée d'un autre type
			else if (random <= 0.75f)
				if (isAngleO) {
					combine(getRandomFrom(angle));
					angleFlag = 1;
				} else {
					combine(getRandomFrom(angleO));
					angleOFlag = 1;
				}

			// 25% de chance de n'avoir qu'un seul angle

			// 35% de chance petit centre
			if (Math.random() <= 0.35f) {
				combine(getRandomFrom(centreS));
				centreFlag = 1;
			}
		} else
			map = new Map("templates/pattern/vide.tmx");
	}

	private Map getRandomFrom(final ArrayList<String> patterns) throws Exception {
		return getRandomFrom(patterns, path);
	}

	public int getCentreFlag() {
		return centreFlag;
	}

	public int getCroixFlag() {
		return croixFlag;
	}

	public int getAngleFlag() {
		return angleFlag;
	}

	public int getAngleOFlag() {
		return angleOFlag;
	}
}
