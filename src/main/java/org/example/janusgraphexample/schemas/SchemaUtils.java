package org.example.janusgraphexample.schemas;

import java.util.Map;

import lombok.experimental.UtilityClass;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;

@UtilityClass
public class SchemaUtils {

    public static void createPropertyKeyIfNotExists(JanusGraphManagement mgmt, String name, Class<?> dataType,
                                             Cardinality cardinality) {
        if (!mgmt.containsPropertyKey(name)) {
            mgmt.makePropertyKey(name).dataType(dataType).cardinality(cardinality).make();
        }
    }

    public static void createCompositeIndexIfNotExists(JanusGraphManagement mgmt, String indexName,
                                                       String... propertyKeys) {
        if (!mgmt.containsGraphIndex(indexName)) {
            var indexBuilder = mgmt.buildIndex(indexName, Vertex.class);
            for (var key : propertyKeys) {
                indexBuilder.addKey(mgmt.getPropertyKey(key));
            }
            indexBuilder.buildCompositeIndex();
        }
    }

    public static void createMixedIndexIfNotExists(JanusGraphManagement mgmt, String indexName, String backend,
                                                   Map<String, Mapping> propertyKeyMappings) {
        if (!mgmt.containsGraphIndex(indexName)) {
            var indexBuilder = mgmt.buildIndex(indexName, Vertex.class);
            for (var entry : propertyKeyMappings.entrySet()) {
                var key = entry.getKey();
                var mapping = entry.getValue();
                if (mapping != null) {
                    indexBuilder.addKey(mgmt.getPropertyKey(key), mapping.asParameter());
                } else {
                    indexBuilder.addKey(mgmt.getPropertyKey(key));
                }
            }
            indexBuilder.buildMixedIndex(backend);
        }
    }

}
