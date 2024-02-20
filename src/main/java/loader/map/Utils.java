package loader.map;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Utils {

    public final static String path = "resources/";

    public static String read(final String internalPath) throws Exception {
        return readAbs(path + internalPath);
    }

    public static String readAbs(final String internalPath) throws Exception {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(internalPath)));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            content.append(line).append("\n");

        content = new StringBuilder(!content.toString().isEmpty() ? content.substring(0, content.lastIndexOf("\n")) : "vide");
        br.close();

        return content.toString();
    }

    public static String path(final String internalPath) {
        return path + internalPath;
    }
}
