package engine.graph.hlib.graphics.paint;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {

	private static final Map<String, Image> imagesMap;

	static {
		imagesMap = new HashMap<>();
	}

	public static Image getImage(final String path) {
		Image image = imagesMap.get(path);

		if (image == null) {
			System.out.println("New image : " + path);
			image = new Image(path);
			imagesMap.put(path, image);
		}

		return image;
	}

	public static boolean haveImage(final String path) {
		return imagesMap.containsKey(path);
	}

	public static void cleanUp(final long ctx) {
		final Collection<Image> images = imagesMap.values();

		for (final Image image : images)
			image.cleanUp(ctx);
	}
}
