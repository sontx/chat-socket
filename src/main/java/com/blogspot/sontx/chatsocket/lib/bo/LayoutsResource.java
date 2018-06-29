package com.blogspot.sontx.chatsocket.lib.bo;

import java.io.InputStream;
import java.net.URL;

public class LayoutsResource extends AbstractResource {
    private static LayoutsResource instance;

    public synchronized static LayoutsResource getInstance() {
        if (instance == null)
            instance = new LayoutsResource();
        return instance;
    }

    public InputStream load(String layoutName) {
        return loadResource("/layouts/" + layoutName);
    }

    public URL getResource(String layoutName) {
        return toUrl("/layouts/" + layoutName);
    }
}
