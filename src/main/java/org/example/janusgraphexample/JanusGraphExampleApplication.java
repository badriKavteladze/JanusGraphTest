package org.example.janusgraphexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JanusGraphExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JanusGraphExampleApplication.class, args);
    }

}
