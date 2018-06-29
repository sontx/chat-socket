package com.blogspot.sontx.chatsocket.lib.bo;

import java.io.InputStream;
import java.net.URL;

abstract class AbstractResource {
    InputStream loadResource(String path) {
        return getClass().getResourceAsStream(path);
    }

    URL toUrl(String path) {
        return getClass().getResource(path);
    }
}
