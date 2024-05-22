package org.example.janusgraphexample.schemas;

import java.util.Map;

import org.janusgraph.core.Cardinality;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;

public class EmployeeSchema implements SchemaDefinition {
    public static final String VERTEX_LABEL = "employee";

    // Properties
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String SKILLS = "skills";

    // Edges
    public static final String EDGE_WORKS_ON = "worksOn";

    @Override
    public void defineSchema(JanusGraphManagement mgmt) {
        if (!mgmt.containsVertexLabel(VERTEX_LABEL)) {
            mgmt.makeVertexLabel(VERTEX_LABEL).make();
        }

        SchemaUtils.createPropertyKeyIfNotExists(mgmt, ID, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, FIRST_NAME, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, LAST_NAME, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, EMAIL, String.class, Cardinality.SINGLE);
        SchemaUtils.createPropertyKeyIfNotExists(mgmt, SKILLS, String.class, Cardinality.SET);

        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "employeeIdIndex", ID);
        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "employeeFirstNameIndex", FIRST_NAME);
        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "employeeLastNameIndex", LAST_NAME);
        SchemaUtils.createCompositeIndexIfNotExists(mgmt, "employeeEmailIndex", EMAIL);

        SchemaUtils.createMixedIndexIfNotExists(mgmt, "employeeSkillsIndex", "search", Map.of(
            SKILLS, Mapping.TEXT
        ));

        if (!mgmt.containsEdgeLabel(EDGE_WORKS_ON)) {
            mgmt.makeEdgeLabel(EDGE_WORKS_ON).multiplicity(Multiplicity.MULTI).make();
        }
    }
}
