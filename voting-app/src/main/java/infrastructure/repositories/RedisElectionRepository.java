package infrastructure.repositories;

import java.util.List;

import org.jboss.logging.Logger;

import domain.Candidate;
import domain.Election;
import domain.ElectionRepository;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.sortedset.SortedSetCommands;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class RedisElectionRepository implements ElectionRepository {
	
	private static final Logger LOGGER = Logger.getLogger(RedisElectionRepository.class);
    private static final String KEY = "election:";
    private final SortedSetCommands<String, String> sortedSetCommands;
    private final KeyCommands<String> keyCommands;

    public RedisElectionRepository(RedisDataSource redisdataSource) {
        sortedSetCommands = redisdataSource.sortedSet(String.class, String.class);
        keyCommands = redisdataSource.key(String.class);
    }

    @Override
    @CacheResult(cacheName = "memoization")
    public Election findById(String id) {
        LOGGER.info("Retrieving election " + id + " from redis");

        List<Candidate> candidates = sortedSetCommands.zrange(KEY + id, 0, -1)
                										.stream()
                										.map(Candidate::new)
                										.toList();
        
        return new Election(id, candidates);
    }

    @Override
    public List<Election> findAll() {
        LOGGER.info("Retrieving elections from redis");
        return keyCommands// .keys(KEY + "election:*").stream()  --------- findById(id.replace(KEY, "elections:")))
        			.keys(KEY + "*").stream()
        			.map(id -> findById(id.replace(KEY, ""))).toList();
    }
    
    @Override
    public void vote(String id, Candidate candidate) {
        LOGGER.info("Voting for " + candidate.id());
        sortedSetCommands.zincrby(KEY + id, 1, candidate.id());
    }

}
