package infrastructure.lifecycle;

import org.jboss.logging.Logger;

import domain.Election;
import infrastructure.repositories.RedisElectionRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class Subscribe {

	private static final org.jboss.logging.Logger LOGGER = Logger.getLogger(Subscribe.class);
	
	public Subscribe(ReactiveRedisDataSource reactiveDataSource, RedisElectionRepository repository) {
		
		LOGGER.info("Startup: Subscribe");
		
		Multi<String> sub = reactiveDataSource.pubsub(String.class).subscribe("elections");
		
		sub.emitOn(Infrastructure.getDefaultWorkerPool()).subscribe().with(id -> {
								         		LOGGER.info("Election " + id + " received from subscription");
								         		Election election = repository.findById(id);
								         		LOGGER.info("Election " + election.id() + " starting");
								         	});
		
		
			// usando RedisDataSource - mas bloqueio a tread por estar sincrona ---------// acima esta o assincrona
		
		// public Subscribe(RedsRedisDataSource redisDataSource, RedisElectionRepository repository) {
//		redisDataSource.pubsub(String.class).subscribe("elections", id -> {
//     		LOGGER.info("Election " + id + " received from subscription");
//			Election election = repository.findById(id);
//     		LOGGER.info("Election " + election.id() + " starting");
//     	});
	}
}
