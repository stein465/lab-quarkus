package infrastructure.rest;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import api.dto.in.Election;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient(configKey="election-management")
public interface ElectionManagement {
	
	@GET
    @Path("/api/elections")
    Uni<List<Election>> getElections();

}
