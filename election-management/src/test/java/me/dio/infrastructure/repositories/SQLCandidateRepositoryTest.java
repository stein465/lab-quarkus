package me.dio.infrastructure.repositories;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import me.dio.domain.CandidateRepository;
import me.dio.domain.CandidateRepositoryTest;
import org.junit.jupiter.api.AfterEach;

@QuarkusTest
class SQLCandidateRepositoryTest extends CandidateRepositoryTest {

    @Inject
    SQLCandidateRepository repository;

    @Inject
    EntityManager entityManager;

    @Override
    public CandidateRepository repository() {
        return repository;
    }

    @AfterEach
    @TestTransaction
    void emptyDB(){
        entityManager.createNativeQuery("DELETE DATABASE candidates");
    }
}