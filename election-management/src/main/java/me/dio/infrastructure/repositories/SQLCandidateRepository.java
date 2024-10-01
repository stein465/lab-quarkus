package me.dio.infrastructure.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import me.dio.domain.Candidate;
import me.dio.domain.CandidateQuery;
import me.dio.domain.CandidateRepository;
import java.util.Optional;

import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class SQLCandidateRepository implements CandidateRepository {
    private final EntityManager entityManager;

    public SQLCandidateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(List<Candidate> candidates) {

        candidates.stream().map(me.dio.infrastructure.repositories.entities.Candidate::fromDomain).forEach(entityManager::merge);
    }

    @Override
    public List<Candidate> find(CandidateQuery query) {
        // TODO Auto-generated method stub
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(me.dio.infrastructure.repositories.entities.Candidate.class);
        var root = cq.from(me.dio.infrastructure.repositories.entities.Candidate.class);
        cq.select(root).where(conditions(query, cb, root));

        return entityManager.createQuery(cq)
                .getResultStream()
                .map(me.dio.infrastructure.repositories.entities.Candidate::toDomain)
                .toList();
    }
    private Predicate[] conditions(CandidateQuery query,
                                   CriteriaBuilder cb,
                                   Root<me.dio.infrastructure.repositories.entities.Candidate> root) {
        return Stream.of(query.ids().map(id -> cb.in(root.get("id")).value(id)),
                        query.name().map(name -> cb.or(cb.like(cb.lower(root.get("familyName")), name.toLowerCase() + "%"),
                                cb.like(cb.lower(root.get("givenName")), name.toLowerCase() + "%"))))
                .flatMap(Optional::stream)
                .toArray(Predicate[]::new);
    }

}
