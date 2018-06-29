package com.blogspot.sontx.chatsocket.lib.bo;

import lombok.extern.log4j.Log4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Manages image files from resources.
 */
@Log4j
public final class ImagesResource extends AbstractResource {
    private static final String IMAGES_DIR = "/images/";

    private static ImagesResource instance;
    private HashMap<String, BufferedImage> imagesHolder = new HashMap<>();

    private ImagesResource() {
        BufferedImage defaultImage = new BufferedImage(48, 48, BufferedImage.TYPE_4BYTE_ABGR);
        imagesHolder.put("default", defaultImage);
    }

    public synchronized static ImagesResource getInstance() {
        if (instance == null)
            instance = new ImagesResource();
        return instance;
    }

    public static void destroyInstance() {
        if (instance != null) {
            instance.release();
            instance = null;
        }
    }

    private void release() {
        for (BufferedImage image : imagesHolder.values()) {
            image.flush();
        }
        imagesHolder.clear();
    }

    private BufferedImage loadImage(String name) throws IOException {
        InputStream imageStream = loadResource(IMAGES_DIR + name);
        BufferedImage image = ImageIO.read(imageStream);
        imagesHolder.put(name, image);
        return image;
    }

    public BufferedImage getImageByName(String name) {
        try {
            return imagesHolder.containsKey(name) ? imagesHolder.get(name) : loadImage(name);
        } catch (IOException e) {
            log.error("Can not read image resource", e);
        }
        return imagesHolder.get("default");
    }

    public URL getImageAsUrl(String name) {
        return toUrl(IMAGES_DIR + name);
    }
}
