package org.example.janusgraphexample.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.example.janusgraphexample.dto.EmployeeDTO.mapVertexToEmployeeDTO;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.example.janusgraphexample.dto.EmployeeDTO;
import org.example.janusgraphexample.dto.UpsertEmployeeReq;
import org.example.janusgraphexample.schemas.EmployeeSchema;
import org.example.janusgraphexample.schemas.ProjectSchema;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final GraphTraversalSource g;

    public EmployeeService(GraphTraversalSource g) {
        this.g = g;
    }

    public Optional<EmployeeDTO> getEmployeeById(String id) {
        var employeeVertex = g.V().has(EmployeeSchema.ID, id).tryNext();
        return employeeVertex.map(EmployeeDTO::mapVertexToEmployeeDTO);
    }

    public EmployeeDTO createEmployee(UpsertEmployeeReq request) {
        var id = UUID.randomUUID().toString();
        var employeeVertex = g.addV(EmployeeSchema.VERTEX_LABEL)
            .property(EmployeeSchema.ID, id)
            .property(EmployeeSchema.FIRST_NAME, request.firstName())
            .property(EmployeeSchema.LAST_NAME, request.lastName())
            .property(EmployeeSchema.EMAIL, request.email())
            .next();

        for (var skill : request.skills()) {
            employeeVertex.property(EmployeeSchema.SKILLS, skill);
        }

        if (request.projectIds() != null) {
            for (var projectId : request.projectIds()) {
                var projectVertex = g.V().has(ProjectSchema.ID, projectId).tryNext();
                if (projectVertex.isEmpty()) {
                    throw new IllegalArgumentException("Project ID " + projectId + " does not exist.");
                } else {
                    employeeVertex.addEdge(EmployeeSchema.EDGE_WORKS_ON, projectVertex.get());
                }
            }
        }

        g.tx().commit();

        return EmployeeDTO.mapVertexToEmployeeDTO(employeeVertex);
    }

    public boolean updateEmployee(String id, UpsertEmployeeReq request) {
        var employeeVertexOpt = g.V().has(EmployeeSchema.ID, id).tryNext();

        if (employeeVertexOpt.isEmpty()) {
            return false;
        }

        var employeeVertex = employeeVertexOpt.get();
        employeeVertex.property(EmployeeSchema.FIRST_NAME, request.firstName());
        employeeVertex.property(EmployeeSchema.LAST_NAME, request.lastName());
        employeeVertex.property(EmployeeSchema.EMAIL, request.email());

        updateSkills(employeeVertex, request.skills());

        g.tx().commit();
        return true;
    }

    public boolean deleteEmployee(String id) {
        var employeeVertexOpt = g.V().has(EmployeeSchema.ID, id).tryNext();

        if (employeeVertexOpt.isEmpty()) {
            return false;
        }

        employeeVertexOpt.get().remove();
        g.tx().commit();
        return true;
    }

    public Set<EmployeeDTO> searchEmployeesBySkill(String skill) {
        var employees = new HashSet<EmployeeDTO>();

        g.V().hasLabel(EmployeeSchema.VERTEX_LABEL)
            .has(EmployeeSchema.SKILLS, skill)
            .forEachRemaining(employeeVertex -> employees.add(mapVertexToEmployeeDTO(employeeVertex)));

        return employees;
    }

    public List<EmployeeDTO> createMultipleEmployees(List<UpsertEmployeeReq> employeeDTOs) {
        return employeeDTOs.stream()
            .map(this::createEmployee)
            .toList();
    }

    public List<EmployeeDTO> getAllEmployees() {
        return g.V().hasLabel(EmployeeSchema.VERTEX_LABEL)
            .toStream()
            .map(EmployeeDTO::mapVertexToEmployeeDTO)
            .toList();
    }

    public boolean assignEmployeeToProject(String employeeId, String projectId) {
        var employeeVertexOpt = g.V().has(EmployeeSchema.ID, employeeId).tryNext();
        var projectVertexOpt = g.V().has(ProjectSchema.ID, projectId).tryNext();

        if (employeeVertexOpt.isEmpty() || projectVertexOpt.isEmpty()) {
            return false;
        }

        var employeeVertex = employeeVertexOpt.get();
        employeeVertex.addEdge(EmployeeSchema.EDGE_WORKS_ON, projectVertexOpt.get());
        return true;
    }

    private void updateSkills(Vertex employeeVertex, Set<String> skills) {
        var existingSkills = new ArrayList<VertexProperty<Object>>();
        employeeVertex.properties(EmployeeSchema.SKILLS).forEachRemaining(existingSkills::add);

        for (var existingSkill : existingSkills) {
            existingSkill.remove();
        }

        for (var skill : skills) {
            employeeVertex.property(EmployeeSchema.SKILLS, skill);
        }
    }
}
