package ru.alaric.thehatgameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class TheHatGameServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheHatGameServerApplication.class, args);
    }

}

// TODO Делать дополнительную проверку на дублирующие списки