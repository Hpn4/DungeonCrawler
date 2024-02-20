package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ImgModif {

	public static void main(final String[] args) {
		// combine("resources/images/mob/fire_worm", "damage", "death", "idle", "walk",
		// "attack");
		// reverseRow("resources/images/mob/fire_worm/walk", 9);

		// mm();
		vec();
		System.exit(0);

		try {
			// extractGIF("resources/images/mob/ghost/2/", 4);
			ImageIO.write(new BufferedImage(1536, 128, BufferedImage.TYPE_4BYTE_ABGR), "PNG",
					new File("resources/furnace.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void vec() {
		final Vector3f vec = new Vector3f(1, 2, 3);

		final HashSet<Vector3f> sets = new HashSet<>();
		for (int x = 0; x < 4; x++)
			for (int y = 0; y < 4; y++)
				for (int z = 0; z < 4; z++) {
					Vector3f tmp = new Vector3f(vec);
					Quaternionf rotation = new Quaternionf();
					rotation.rotateXYZ((float) Math.toRadians(x * 90f), (float) Math.toRadians(y * 90f),
							(float) Math.toRadians(z * 90f));
					tmp.rotate(rotation);
					sets.add(tmp);
				}
		
		System.out.println(sets.size());
		sets.forEach(System.out::println);
	}

	public static long gcd(long a, long h) {
		long temp;
		while (true) {
			temp = a % h;
			if (temp == 0)
				return h;
			a = h;
			h = temp;
		}
	}

	/*
	 * Iterative Function to calculate (x^y)%p in O(log y)
	 */
	public static long power(long x, long y, long p) {
		// Initialize result
		long res = 1;

		// Update x if it is more
		// than or equal to p
		x = x % p;

		while (y > 0) {
			// If y is odd, multiply x
			// with result
			if ((y & 1) == 1)
				res = (res * x) % p;

			// y must be even now
			// y = y / 2
			y = y >> 1;
			x = (x * x) % p;
		}
		return res;
	}

	public static long exponentMod(long A, long B, long C) {

// Base cases
		if (A == 0)
			return 0;
		if (B == 0)
			return 1;

// If B is even
		long y;
		if ((B & 1) == 0) {
			y = exponentMod(A, B >> 1, C);
			y = (y * y) % C;
		}

// If B is odd
		else {
			y = A % C;
			y = (y * exponentMod(A, B - 1, C) % C) % C;
		}

		return (y + C) % C;
	}

	public static void mm() {

		// Returns gcd of a and b/ Two random prime numbers
		long p = 9967;
		long q = 9973;

		// First part of public key:
		long n = p * q;

		// Finding other part of public key.
		// e stands for encrypt
		long e = 2;
		long phi = (p - 1) * (q - 1);
		while (e < phi) {
			// e must be co-prime to phi and
			// smaller than phi.
			if (gcd(e, phi) == 1)
				break;
			else
				e++;
		}

		// Private key (d stands for decrypt)
		// choosing d such that it satisfies
		// d*e = 1 + k * totient
		int k = 2; // A constant value
		long d = (1 + k * phi);

		System.out.println("Public key : " + e + ", n:" + n);
		System.out.println("Private key : " + (d / e) + ", mod:" + (d % e) + ", n:" + n);
		n = 99400891;
		e = 36199003;

		// Message to be encrypted
		long msg = 281237;

		System.out.println("Encrypted : " + exponentMod(msg, e, n));
		/**
		 * System.out.println("Message data = " + msg);
		 * 
		 * // Encryption c = (msg ^ e) % n double c = Math.pow(msg, e); c = fmod(c, n);
		 * printf("\nEncrypted data = %lf", c);
		 * 
		 * // Decryption m = (c ^ d) % n double m = pow(c, d); m = fmod(m, n);
		 * printf("\nOriginal Message Sent = %lf", m);
		 */

	}

	public static void extractGIF(final String dir, final int scale) throws IOException {
		final int startX = 0;
		final int startY = 12;
		final int reduceHeight = 44;
		final int reduceWidth = 12;

		File directory = new File(dir);
		String[] files = directory.list();

		String path = directory.getAbsolutePath() + "/";
		final ArrayList<String> names = new ArrayList<>();

		for (final String file : files) {
			if (!file.startsWith(".") && !new File(path + file).isDirectory()) {
				final String dirName = file.substring(0, file.indexOf("."));
				names.add(dirName);
				Files.createDirectories(Paths.get(dir + dirName));
				Runtime.getRuntime()
						.exec("/usr/local/bin/convert " + path + file + " -coalesce " + path + dirName + "/%d.png");
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Contient le nom de la texture et sa largeur
		final HashMap<String, Integer> maps = new HashMap<>();

		for (final String dire : names) {
			path = dir + dire + "/";
			directory = new File(path);
			files = directory.list();
			Arrays.sort(files, (o1, o2) -> {
				return Integer.valueOf(o1.substring(0, o1.indexOf(".")))
						.compareTo(Integer.valueOf(o2.substring(0, o2.indexOf("."))));
			});

			BufferedImage img = ImageIO.read(new File(path + files[0]));

			final int width = (img.getWidth() - startX - reduceWidth) / scale;
			final int height = (img.getHeight() - startY - reduceHeight) / scale;

			final BufferedImage row = new BufferedImage(width * files.length, height, BufferedImage.TYPE_4BYTE_ABGR);

			final int rgb = img.getRGB(0, 0);
			for (int i = 0; i < files.length; i++) {
				img = ImageIO.read(new File(path + "/" + files[i]));

				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						int col = img.getRGB((x + startX) * scale, (y + startY) * scale);

						if (col == rgb)
							col = getRGBA(0, 0, 0, 0);

						row.setRGB(x + i * width, y, col);
					}
			}

			ImageIO.write(row, "PNG", new File(path + "row.png"));
			reverseRow(path + "row", files.length, dir + dire + ".png");

			maps.put(dire, row.getWidth());
		}

		// On trie par valeur (width)
		final ArrayList<Entry<String, Integer>> list = new ArrayList<>(maps.entrySet());
		list.sort(Entry.comparingByValue());

		final ArrayList<String> orderredNames = new ArrayList<>();
		for (final Entry<String, Integer> entry : list)
			orderredNames.add(entry.getKey());

		// On assemble le tout en fonction de la longueur
		combine(dir, orderredNames.toArray(String[]::new));
	}

	public static void combine(final String path, final String... end) {
		try {
			final int size = end.length;
			final BufferedImage[] imgs = new BufferedImage[size];

			int maxWid = 0;
			int height = 0;
			for (int i = 0; i < size; i++) {
				imgs[i] = ImageIO.read(new File(path + "/" + end[i] + ".png"));

				height += imgs[i].getHeight();

				final int width = imgs[i].getWidth();
				if (width > maxWid)
					maxWid = width;
			}

			final BufferedImage total = new BufferedImage(maxWid, height, BufferedImage.TYPE_4BYTE_ABGR);

			int yPos = 0;
			for (int i = 0; i < size; i++) {
				final BufferedImage tmp = imgs[i];

				for (int x = 0; x < tmp.getWidth(); x++)
					for (int y = 0; y < tmp.getHeight(); y++) {
						final int rgb = tmp.getRGB(x, y);
						total.setRGB(x, yPos + y, rgb);
					}

				yPos += tmp.getHeight();
			}

			ImageIO.write(total, "PNG", new File(path + "/combine.png"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void inRow(final String name, final int rows) {
		try {
			final BufferedImage img = ImageIO.read(new File(name + ".png"));
			final BufferedImage img2 = ImageIO.read(new File(name + "-2.png"));

			final int width = img.getWidth();
			final int height = img.getHeight();

			final int sectWidth = img.getWidth(), sectHeight = height / rows;

			final BufferedImage total = new BufferedImage(sectWidth * rows, sectHeight * 2,
					BufferedImage.TYPE_4BYTE_ABGR);

			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					final int rgb = img.getRGB(x, y);
					final int rgb2 = img2.getRGB(x, y);

					final int sect = y / sectHeight, yPos = y - (sect * sectHeight);

					total.setRGB(x + sect * sectWidth, yPos, rgb);
					total.setRGB(x + sect * sectWidth, yPos + sectHeight, rgb2);
				}

			ImageIO.write(total, "PNG", new File(name + "-3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void forEach() {
		try {
			final BufferedImage img = ImageIO.read(new File("image1.png"));

			final int d = 240;
			final int width = img.getWidth();
			final int height = img.getHeight();

			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					final int rgb = img.getRGB(x, y);
					final int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = (rgb >> 0) & 0xFF;
					if (g > d && r > d && b > d)
						img.setRGB(x, y, getRGBA(r, g, b, 0));
				}

			ImageIO.write(img, "PNG", new File("bat-2.png"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void reverseRow(final String file, final int cols, final String dst) {
		try {
			final BufferedImage img = ImageIO.read(new File(file + ".png"));

			final int width = img.getWidth();
			final int height = img.getHeight();

			final int sectWid = width / cols;

			final BufferedImage img2 = new BufferedImage(width, height * 2, BufferedImage.TYPE_4BYTE_ABGR);

			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					final int rgb = img.getRGB(x, y);

					final int sect = x / sectWid, xPos = x - (sect * sectWid);

					img2.setRGB(x, y, rgb);
					img2.setRGB(sectWid - 1 - xPos + sect * sectWid, y + height, rgb);
				}

			ImageIO.write(img2, "PNG", new File(dst));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void reverse(final String file, final String newFile) {
		try {
			final BufferedImage img = ImageIO.read(new File(file));

			final int width = img.getWidth();
			final int height = img.getHeight();

			final BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					final int rgb = img.getRGB(x, y);
					img2.setRGB(width - 1 - x, y, rgb);
				}

			ImageIO.write(img2, "PNG", new File(newFile));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static int getRGBA(final int r, final int g, final int b, final int a) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
	}
}
