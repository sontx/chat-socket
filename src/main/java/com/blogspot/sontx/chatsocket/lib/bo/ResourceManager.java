package com.blogspot.sontx.chatsocket.lib.bo;

import lombok.extern.log4j.Log4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@Log4j
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
		InputStream imageStream = getClass().getResourceAsStream("/images/" + name);
		BufferedImage image = ImageIO.read(imageStream);
		imagesHolder.put(name, image);
		return image;
	}

	public BufferedImage getImageByName(String name) {
		try {
			return imagesHolder.containsKey(name) ? imagesHolder.get(name) : loadImage(name);
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
}
