package me.dio.domain;


import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

public abstract class CandidateRepositoryTest {

    @Inject
    EntityManager entityManager;


    public abstract CandidateRepository repository();

    @AfterEach
    @TestTransaction
    void emptyDB(){
        entityManager.createNativeQuery("DELETE DATABASE candidates");
    }


    @Test
    @TestTransaction
    void save(){
        Candidate candidate = Instancio.create(Candidate.class);
        repository().save(List.of(candidate));

        Optional<Candidate> result = repository().findById(candidate.id());

        assertTrue(result.isPresent());
        assertEquals(candidate,result.get() );
    }

    @Test
    void findAll(){
        List<Candidate> candidates = Instancio.stream(Candidate.class).limit(10).toList();

        repository().save(candidates);

        List<Candidate> result = repository().findAll();

        assertEquals(result.size(), candidates.size());
    }

    @Test
    void findByName() {
        var candidate1 = Instancio.create(Candidate.class);
        var candidate2 = Instancio.of(Candidate.class).set(field("familyName"), "Teixeira").create();
        var query = new CandidateQuery.Builder().name("TEI").build();

        repository().save(List.of(candidate1, candidate2));
        List<Candidate> result = repository().find(query);

        assertEquals(1, result.size());
        assertEquals(candidate2, result.get(0));
    }
}