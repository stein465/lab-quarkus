package me.dio.domain;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public interface CandidateRepository {

    List<Candidate> find (CandidateQuery query);

    void save(List<Candidate> candidates);

    default List<Candidate> findAll(){
        return find(new CandidateQuery.Builder().build());
    }


    default Optional<Candidate> findById(String id){

        CandidateQuery query = new CandidateQuery.Builder().ids(Set.of(id)).build();
        List<Candidate> candidates = find(query);

        return candidates != null && !candidates.isEmpty() ? Optional.of(candidates.get(0)) : Optional.empty();
    }

}
