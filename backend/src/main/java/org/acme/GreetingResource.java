package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Collection;

@Path("/hello")
public class GreetingResource {

    @Inject
    JsonWebToken accessToken;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String userId = accessToken.getClaim("preferred_username");
        Collection<String> groups = accessToken.getClaim("groups");
        return String.format(
                "Hello from RESTEasy Reactive. You are %s, and have groups %s",
                userId, groups);
    }
}
