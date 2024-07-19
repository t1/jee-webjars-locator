package com.github.t1.locator.webjars;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Serve files in <code>resources/META-INF/static</code>.
 * This would work out-of-the-box, if the {@link jakarta.ws.rs.ApplicationPath} was not empty.
 */
@Path("/static")
@ApplicationScoped
public class StaticFilesResource {
    private final StaticFilesLoader files = new StaticFilesLoader("static", "META-INF/static/");

    @GET
    @Path("/{filePath:.*}")
    public Response getStaticResource(@PathParam("filePath") String filePath) {
        return files.response(filePath);
    }
}
