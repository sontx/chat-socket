package in.sontx.tut.chatsocket.bo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public final class ResourceManager {
	private static ResourceManager instance;

	public static ResourceManager getInstance() {
		if (instance == null)
			instance = new ResourceManager();
		return instance;
	}

	public static void destroyInstance() {
		if (instance != null) {
			instance.release();
			instance = null;
		}
	}

	private HashMap<String, BufferedImage> imagesHolder = new HashMap<>();

	private void release() {
		for (BufferedImage image : imagesHolder.values()) {
			image.flush();
		}
		imagesHolder.clear();
	}

	private BufferedImage loadImage(String name) throws IOException {
		InputStream imageStream = getClass().getResourceAsStream("/" + name);
		BufferedImage image = ImageIO.read(imageStream);
		imagesHolder.put(name, image);
		return image;
	}

	public BufferedImage getImageByName(String name) {
		try {
			return imagesHolder.containsKey(name) ? imagesHolder.get(name) : loadImage(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
