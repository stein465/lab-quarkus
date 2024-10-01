package infrastructure.lifecycle;

import org.jboss.logging.Logger;

import infrastructure.repositories.RedisElectionRepository;

public class Cache {

	private static final Logger LOGGER = Logger.getLogger(Cache.class);

    public Cache(RedisElectionRepository repository) {
        LOGGER.info("Startup: Cache");
        repository.findAll();
    }
}
