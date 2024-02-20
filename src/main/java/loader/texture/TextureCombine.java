package loader.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.system.MemoryUtil;

public class TextureCombine {

	public static Texture combine(final String... imgPaths) throws Exception {
		final long time = System.nanoTime();
		// La première image est l'image de fond
		final BufferedImage base = ImageIO.read(new File(imgPaths[0]));
		final int w = base.getWidth(), h = base.getHeight();

		for (int i = 1; i < imgPaths.length; i++) {
			final BufferedImage draw = ImageIO.read(new File(imgPaths[i]));

			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++) {
					final int rgb = draw.getRGB(x, y);
					final int alpha = (rgb >> 24) & 0xff;

					if (alpha != 0) {
						final int rgb1 = base.getRGB(x, y);
						// Stored
						final float r = ((rgb1 >> 16) & 0xff) / 255.0f;
						final float g = ((rgb1 >> 8) & 0xff) / 255.0f;
						final float b = ((rgb1 >> 0) & 0xff) / 255.0f;
						final float a = ((rgb1 >> 24) & 0xff) / 255.0f;

						// Over
						final float r1 = ((rgb >> 16) & 0xff) / 255.0f;
						final float g1 = ((rgb >> 8) & 0xff) / 255.0f;
						final float b1 = ((rgb >> 0) & 0xff) / 255.0f;
						final float a1 = ((float) alpha) / 255.0f;

						// Factor
						final float fac = a - a1;

						// Blending
						final float r2 = r1 * a1 + r * fac;
						final float g2 = g1 * a1 + g * fac;
						final float b2 = b1 * a1 + b * fac;
 
						final int end = ((255 & 0xFF) << 24) | (((byte) (r2 * 255) & 0xFF) << 16)
								| (((byte) (g2 * 255) & 0xFF) << 8) | (((byte) (b2 * 255) & 0xFF) << 0);

						base.setRGB(x, y, end);
					}
				}
		}

		// width * height * canaux (RGBA)
		final ByteBuffer img = MemoryUtil.memAlloc(w * h * 4);

		int index = 0;
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++) {
				final int rgb = base.getRGB(x, y);

				img.put(index++, (byte) ((rgb >> 16) & 0xff));
				img.put(index++, (byte) ((rgb >> 8) & 0xff));
				img.put(index++, (byte) ((rgb >> 0) & 0xff));
				img.put(index++, (byte) ((rgb >> 24) & 0xff));
			}

		final Texture text = new Texture(w, h, img.position(0));

		MemoryUtil.memFree(img);

		System.out.println("Time : " + (System.nanoTime() - time) / 1000000.0d);
		return text;
	}
}
