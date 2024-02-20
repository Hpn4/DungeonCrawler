package loader.generator;

import java.io.File;
import java.util.ArrayList;

import loader.map.AbstractLayer;
import loader.map.Layer;
import loader.map.Map;
import loader.map.Tile;

public class MapMaker extends AbstractRoomMaker {

    private final static String pathVegetation = "templates/vegetation/";

    private final static ArrayList<String> vegetationTL;

    private final static ArrayList<String> vegetationTR;

    private final static int[] herbes = {69, 420, 421, 140, 443, 444, 92, 466, 467, 115, 489, 490};

    private final static int[] decos = {69, 70, 71, 118, 351, 420, 421, 328, 92, 93, 94, 141, 142, 443, 444, 329, 115,
            116, 117, 119, 120, 467, 466, 139, 140, 138, 143, 169, 490, 489,};

    // On récupère tous les patterns différents
    static {
        vegetationTL = new ArrayList<>();
        vegetationTR = new ArrayList<>();

        final String[] files = new File(pathVegetation).list();

        for (final String file : files) {
            // On ajoute en haut � gauche et � droite les angles de vegetation
            if (file.startsWith("tl_"))
                vegetationTL.add(file);
            else if (file.startsWith("tr_"))
                vegetationTR.add(file);
        }
    }

    // Salle == 0 > normale, 1 > Mine
    public MapMaker(final int porteS, final int porteN, final int porteO, final int porteE, final int salle)
            throws Exception {

        final ArrayList<String> porteAdded = new ArrayList<>();
        if (porteS == 1)
            porteAdded.add("bot.tmx");
        if (porteN == 1)
            porteAdded.add("top.tmx");
        if (porteO == 1)
            porteAdded.add("left.tmx");
        if (porteE == 1)
            porteAdded.add("right.tmx");

        Map base;

        // On a deux portes
        if (porteAdded.size() == 2)
            map = new TwoDoorRoomMaker(porteO == 1, porteN == 1, porteE == 1, porteS == 1).getFinalMap();
        else
            map = new AnyDoorRoomMaker().getFinalMap();

        if (porteAdded.size() == 4) {
            base = new Map("templates/portes/4.tmx");
            map.setPorte(true, true, true, true);
        } else {
            base = new Map("templates/" + (salle == 0 ? "" : "mine") + "/base.tmx");

            // On ajoute le layer pour les portes
            base.getLayers().add(new Layer(map.getWidth(), map.getHeight(), "porte"));

            for (final String porteF : porteAdded) {
                switch (porteF) {
                    case "top.tmx":
                        map.setPorteN(true);
                        break;
                    case "bot.tmx":
                        map.setPorteS(true);
                        break;
                    case "left.tmx":
                        map.setPorteO(true);
                        break;
                    case "right.tmx":
                        map.setPorteE(true);
                        break;
                }
                combine(base, new Map("templates/portes/" + porteF));
            }
            // On met à jour les layers et met de la déco
            map.add(base.getTilesets(), base.getLayers());

            //if (salle == 1)
            //	mine(map);
        }

        genDeco();
        // if (salle == 0)
        // genVegetation(flags);
    }

    protected void genVegetation(final int[] flags) throws Exception {
        // 0 : centre, 1 : croix, 2 : angle, 3 : angle O
        // Angle normaux prennent le TR
        // Angle O pennent l'angle Top Left
        if (flags[1] == 0) {
            Map m = null;

            if (flags[2] == 0 && Math.random() <= 0.4f)
                m = getRandomFrom(vegetationTL, pathVegetation);

            else if (flags[3] == 0 && Math.random() <= 0.4f)
                m = getRandomFrom(vegetationTR, pathVegetation);

            if (m != null) {
                map.add(m.getTilesets(), m.getLayers());
            }
        }
    }

    private void genDeco() {
        final AbstractLayer s = map.getLayers().get(0);

        // De la bheeuuu
        final int w = map.getWidth() - 3, h = map.getHeight() - 5;
        for (int i = 0; i < 30; i++) {
            int x = Math.round(1 + (float) Math.random() * w);
            int y = Math.round(4 + (float) Math.random() * h);

            final Tile tile = s.get(x, y);
            if (tile == Tile.AIR) {
                i++;
                final int index = Math.round((float) Math.random() * (herbes.length - 1));
                s.set(x, y, new Tile(herbes[index], 0));
            }
        }

        // Des petits arbustes
        for (int i = 0; i < 0; i++) {
            int x = Math.round(1 + (float) Math.random() * w);
            int y = Math.round(4 + (float) Math.random() * h);

            final Tile tile = s.get(x, y);
            if (tile == Tile.AIR) {
                final int index = Math.round((float) Math.random() * (decos.length - 1));
                s.set(x, y, new Tile(decos[index], 0));
            }
        }
    }

    private static final int[] CHARBON = {70, 90, 130, 150, 10, 10, 70};

    private static final int[] FER = {71, 91, 131, 151, 11, 51, 71};

    private static final int[] ARGENT = {72, 92, 132, 152, 12, 52, 112};

    private static final int[] CUIVRE = {73, 93, 133, 153, 13, 53, 113};

    private static final int[] METEORITE = {74, 94, 134, 154, 14, 54, 114};

    private static final int[] OR = {75, 95, 135, 155, 15, 55, 115};

    private static final int[] DIMERITIUM = {76, 96, 136, 156, 36, 56, 116};

    private void mine(final Map map) {
        final AbstractLayer l = map.getLayer("special");

        for (int x = 1; x < map.getWidth() - 3; x++)
            for (int y = 4; y < map.getHeight() - 1; y++) {
                if (l.get(x, y).getTile() == 36) { // Minerais à remplacer
                    float random = (float) Math.random();
                    final int index = (int) (Math.random() * (OR.length - 1));
                    int tile;

                    if (random <= 0.30f) // 30%
                        tile = FER[index];
                    else if (random <= 0.55f) // 25%
                        tile = CHARBON[index];
                    else if (random <= 0.75f) // 20%
                        tile = CUIVRE[index];
                    else if (random <= 0.85f) // 10%
                        tile = ARGENT[index];
                    else if (random <= 0.93f) // 8%
                        tile = OR[index];
                    else if (random <= 0.98) // 5%
                        tile = DIMERITIUM[index];
                    else // 2%
                        tile = METEORITE[index];

                    l.get(x, y).setTile(tile);
                }
            }

    }
}
