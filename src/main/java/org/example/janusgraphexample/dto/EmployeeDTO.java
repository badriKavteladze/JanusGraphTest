package org.example.janusgraphexample.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.example.janusgraphexample.schemas.EmployeeSchema;
import org.example.janusgraphexample.schemas.ProjectSchema;

public record EmployeeDTO(String id, String firstName, String lastName, String email, Set<String> skills,
                          List<String> projectIds) {

    public static EmployeeDTO mapVertexToEmployeeDTO(Vertex employeeVertex) {
        var id = employeeVertex.property(EmployeeSchema.ID).value().toString();
        var firstName = employeeVertex.property(EmployeeSchema.FIRST_NAME).value().toString();
        var lastName = employeeVertex.property(EmployeeSchema.LAST_NAME).value().toString();
        var email = employeeVertex.property(EmployeeSchema.EMAIL).value().toString();
        var skills = new HashSet<String>();
        employeeVertex.properties(EmployeeSchema.SKILLS).forEachRemaining(p -> skills.add(p.value().toString()));

        var projectIds = new ArrayList<String>();
        employeeVertex.edges(Direction.OUT, EmployeeSchema.EDGE_WORKS_ON).forEachRemaining(edge -> {
            Vertex projectVertex = edge.inVertex();
            projectIds.add(projectVertex.property(ProjectSchema.ID).value().toString());
        });

        return new EmployeeDTO(id, firstName, lastName, email, skills, projectIds);
    }
}

