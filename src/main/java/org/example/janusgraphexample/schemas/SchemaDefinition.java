package org.example.janusgraphexample.schemas;

import org.janusgraph.core.schema.JanusGraphManagement;

public interface SchemaDefinition {
    void defineSchema(JanusGraphManagement mgmt);
}
