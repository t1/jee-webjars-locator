package com.github.t1.locator.webjars;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML_TYPE;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static jakarta.ws.rs.core.MediaType.valueOf;

@Slf4j
@RequiredArgsConstructor
class StaticFilesLoader {
    protected final String name;
    private final String prefix;

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    Response response(String filePath) {
        String path = prefix + filePath;
        log.trace("look for static file: {}: {}: {}", name, prefix, filePath);
        InputStream stream = classLoader().getResourceAsStream(path);
        if (stream == null)
            return null;
        log.trace("found {} in {}", filePath, name);
        return Response.ok(stream).type(type(fileSuffix(filePath))).build();
    }

    private static String fileSuffix(String filePath) {
        if (filePath == null)
            return null;
        int i = filePath.lastIndexOf('.');
        if (i < 0)
            return null;
        return filePath.substring(i);
    }

    private static MediaType type(String fileSuffix) {
        if (fileSuffix == null)
            return null;
        return switch (fileSuffix) {
            case ".css" -> valueOf("text/css");
            case ".html" -> TEXT_HTML_TYPE;
            case ".gif" -> valueOf("image/gif");
            case ".ico" -> valueOf("image/x-icon");
            case ".jpeg" -> valueOf("image/jpeg");
            case ".png" -> valueOf("image/png");
            case ".raml" -> valueOf("application/raml+yaml");
            case ".otf" -> valueOf("font/opentype");
            case ".ttf" -> valueOf("font/truetype");
            case ".woff" -> valueOf("font/x-font-woff");
            case ".woff2" -> valueOf("font/x-font-woff2");
            default -> TEXT_PLAIN_TYPE;
        };
    }

    static ClassLoader classLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = StaticFilesLoader.classLoader();
        return loader;
    }
}
