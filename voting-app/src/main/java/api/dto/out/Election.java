package api.dto.out;

import java.util.List;

import domain.Candidate;

public record Election(String id, List<String> candidates) {
	
	 public static Election fromDomain(domain.Election election) {
	    //    return new Election(election.id(), election.candidates().stream().map(Candidate::id).toList());
		 return new Election(election.id(), election.candidates().stream().map(candidate -> candidate.id()).toList());
	    }
}
