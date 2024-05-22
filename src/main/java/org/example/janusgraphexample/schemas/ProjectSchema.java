package org.example.janusgraphexample.schemas;

import java.util.Map;

import org.janusgraph.core.Cardinality;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;

public class ProjectSchema implements SchemaDefinition {
    public static final String VERTEX_LABEL = "project";

    // Properties
    public static final String ID = "id";
    public static final String PROJECT_NAME = "projectName";
    public static final String BUDGET = "budget";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    @Override
    public void defineSchema(JanusGraphManagement mgmt) {
        if (!mgmt.containsVertexLabel(VERTEX_LABEL)) {
            mgmt.makeVertexLabel(VERTEX_LABEL).make();
        }

        SchemaUtils.createPropertyKeyIfNotExists(mgmt, ID, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, PROJECT_NAME, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, BUDGET, Double.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, START_DATE, Long.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, END_DATE, Long.class, Cardinality.SINGLE);

        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "projectIdIndex", ID);
        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "projectNameIndex", PROJECT_NAME);

        SchemaUtils.createMixedIndexIfNotExists(mgmt, "projectDateIndex", "search", Map.of(
            START_DATE, Mapping.DEFAULT,
            END_DATE, Mapping.DEFAULT
        ));
        SchemaUtils.createMixedIndexIfNotExists(mgmt, "projectBudgetIndex", "search", Map.of(
            BUDGET, Mapping.DEFAULT
        ));
    }
}


