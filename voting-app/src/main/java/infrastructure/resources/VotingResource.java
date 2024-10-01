package infrastructure.resources;

import java.util.List;

import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import api.ElectionApi;
import api.dto.out.Election;
import domain.ElectionRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/api/voting")
public class VotingResource {
	
//	private final ElectionApi api;

	private final ElectionRepository repository;
	
	
    /**
	 * @param repository
	 */
	public VotingResource(ElectionRepository repository) {
	//	this.api = null;
		this.repository = repository;
	}

//	public VotingResource(ElectionApi api) {
//        this.api = api;
//		this.repository = null;
//    }

//    @GET
//    public List<Election> candidates() {
//        return api.findAll();
//    }
	
  @GET
  public List<domain.Election> findAll() {
      return repository.findAll();
  }

    @POST
    @Path("/elections/{electionId}/candidates/{candidateId}")
    @ResponseStatus(RestResponse.StatusCode.ACCEPTED)
    @Transactional
    public void vote(@PathParam("electionId") String electionId, @PathParam("candidateId") String candidateId) {
    	
    	
    	domain.Election election = repository.findById(electionId);
    	election.candidates().stream().filter(candidate -> candidate.id().equals(candidateId))
    						.findFirst().ifPresent(candidate -> repository.vote(electionId, candidate));
    	
    	
    	
 //       api.vote(electionId, candidateId);
    }

}
