package org.acme;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

@Path("/")
public class GreetingResource {
    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        String userId = securityIdentity.getPrincipal().getName();
        Collection<String> groups = securityIdentity.getRoles();
        return String.format(
                "Hello, you are %s, with groups %s",
                userId, groups.stream().sorted().collect(joining(", ")));
    }

    @GET
    @Path("/name")
    @Produces(MediaType.TEXT_PLAIN)
    public String name() {
        return securityIdentity.getPrincipal().getName();
    }
}
