package loader.texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {

	private static final Map<String, AbstractTexture> texturesMap;

	static {
		texturesMap = new HashMap<>();
	}

	public static AbstractTexture getTexture(final String path) throws Exception {
		AbstractTexture texture = texturesMap.get(path);

		if (texture == null) {
			texture = new Texture(path);
			texturesMap.put(path, texture);
		}

		return texture;
	}

	public static boolean haveTexture(final String path) {
		return texturesMap.containsKey(path);
	}

	public static AbstractTexture putTexture(final String key, final AbstractTexture value) {
		return texturesMap.put(key, value);
	}

	public static void cleanup() {
		for (final AbstractTexture texture : texturesMap.values())
			texture.cleanup();
	}
}
