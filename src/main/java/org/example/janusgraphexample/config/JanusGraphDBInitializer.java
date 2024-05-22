package org.example.janusgraphexample.config;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
public class JanusGraphDBInitializer {

    @Value("${janus-db.config.location}")
    private Resource dbConfigFile;

    private JanusGraph graph;

    @PostConstruct
    public void init() throws IOException {
        var path = dbConfigFile.getFile().toPath();
        graph = JanusGraphFactory.open(path.toString());
    }

    @PreDestroy
    public void destroy() {
        graph.close();
    }

    @Bean
    public JanusGraph getGraph() {
        return graph;
    }

    @Bean
    public GraphTraversalSource graphTraversalSource() {
        return graph.traversal();
    }
}
