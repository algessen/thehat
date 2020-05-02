package ru.alaric.thehatgameserver.embeddedredis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class EmbeddedRedisTestConfiguration {
    private final redis.embedded.RedisServer redisServer;

    public EmbeddedRedisTestConfiguration(@Value("${spring.redis.port}") final int redisPort) {
        this.redisServer = new redis.embedded.RedisServer(redisPort);
    }

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }
}
