package engine.graph.renderer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Options {

    public static boolean renderShadows;

    public static boolean activeSSAO;

    public static boolean showFps;

    public static boolean vSync;

    public static int fontHeight;

    public static void readOptions() {
        try {
            final ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/config.dat"));

            renderShadows = ois.readBoolean();
            activeSSAO = ois.readBoolean();
            showFps = ois.readBoolean();
            vSync = ois.readBoolean();
            fontHeight = ois.readInt();

            ois.close();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void writeOptions() {
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/config.dat"));

            oos.writeBoolean(renderShadows);
            oos.writeBoolean(activeSSAO);
            oos.writeBoolean(showFps);
            oos.writeBoolean(vSync);
            oos.writeInt(fontHeight);

            oos.close();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
