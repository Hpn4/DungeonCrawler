package loader.generator;

import java.io.File;
import java.util.ArrayList;

public class Vegetation {

    private final static String path = "templates/vegetation/";

    private final static ArrayList<String> topLeft;

    private final static ArrayList<String> topRight;

    private final static ArrayList<String> botLeft;

    private final static ArrayList<String> botRight;

    private final static ArrayList<String> center;

    private final static ArrayList<String> bigCenter;

    static {
        final String[] files = new File(path).list();

        topLeft = new ArrayList<>();
        topRight = new ArrayList<>();
        botLeft = new ArrayList<>();
        botRight = new ArrayList<>();
        center = new ArrayList<>();
        bigCenter = new ArrayList<>();

        for (final String file : files) {
            if (file.startsWith("tl"))
                topLeft.add(file);
            else if (file.startsWith("tr"))
                topRight.add(file);
            else if (file.startsWith("bl"))
                botLeft.add(file);
            else if (file.startsWith("br"))
                botRight.add(file);
            else if (file.startsWith("center"))
                center.add(file);
            else if (file.startsWith("big"))
                bigCenter.add(file);
        }
    }
}
