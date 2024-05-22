package org.example.janusgraphexample.schemas;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.janusgraph.core.JanusGraph;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchemaManager {

    private final JanusGraph graph;

    @PostConstruct
    public void createSchema() {
        var mgmt = graph.openManagement();

        new EmployeeSchema().defineSchema(mgmt);
        new ProjectSchema().defineSchema(mgmt);

        mgmt.commit();
    }
}

