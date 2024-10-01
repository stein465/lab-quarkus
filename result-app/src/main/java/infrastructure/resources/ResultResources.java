package infrastructure.resources;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

import api.dto.in.Election;
import infrastructure.rest.ElectionManagement;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ResultResources {

	private final ElectionManagement electionManagement;

    public ResultResources(@RestClient ElectionManagement electionManagement) {
        this.electionManagement = electionManagement;
    }

    @GET
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<List<Election>> results() {
    	
    	
        return Multi.createFrom()
                    .ticks()
                    //.every(Duration.ofSeconds(5))
                    .every(Duration.of(5, ChronoUnit.SECONDS))
                    .onItem()
                    .transformToMultiAndMerge(n -> electionManagement.getElections().toMulti());
    }
}
