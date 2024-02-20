package game.item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class ItemReader {

	private HashMap<String, Object> datas;

	public ItemReader(final String path) {
		try {
			Files.lines(Paths.get("resources/" + path)).forEach(this::registerLine);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void registerLine(final String line) {
		final int index = line.indexOf(":");
		final String key = line.substring(0, index);
		final String strValue = line.substring(index + 1);

		Object val = strValue;

		try {
			val = Float.parseFloat(strValue);
		} catch (final NumberFormatException e) {
			try {
				val = Integer.parseInt(strValue);
			} catch (final NumberFormatException e1) {
				try {
					val = Rarity.valueOf(strValue.toUpperCase());
				} catch (final InvalidParameterException e2) {
					val = Boolean.parseBoolean(strValue);
				}
			}
		}

		datas.put(key, val);
	}

	public String get(final String key) {
		if (datas.containsKey(key))
			return (String) datas.get(key);

		return "";
	}

	public Rarity getRarity(final String key) {
		if (datas.containsKey(key))
			return (Rarity) datas.get(key);

		return Rarity.COMMON;
	}

	public int getInt(final String key) {
		if (datas.containsKey(key))
			return (int) datas.get(key);

		return 0;
	}

	public float getFloat(final String key) {
		if (datas.containsKey(key))
			return (float) datas.get(key);

		return 0;
	}

	public boolean getBool(final String key) {
		return getBool(key, false);
	}

	/**
	 * 
	 * @param key          La clé de la valeur
	 * @param defaultValue La valeur par défaut à retourner si la valeur n'est pas
	 *                     dans le fichier
	 * @return
	 */
	public boolean getBool(final String key, final boolean defaultValue) {
		if (datas.containsKey(key))
			return (boolean) datas.get(key);

		return defaultValue;
	}
}
