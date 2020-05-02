package ru.alaric.thehatgameserver.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.redis")
@Data
@NoArgsConstructor
public class RedisProperties {
    private String host;
    private int port = 6379;
}
