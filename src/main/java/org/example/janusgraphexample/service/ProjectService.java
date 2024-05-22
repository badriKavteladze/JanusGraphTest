package org.example.janusgraphexample.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.example.janusgraphexample.dto.EmployeeDTO;
import org.example.janusgraphexample.dto.ProjectDTO;
import org.example.janusgraphexample.dto.UpsertProjectReq;
import org.example.janusgraphexample.schemas.EmployeeSchema;
import org.example.janusgraphexample.schemas.ProjectSchema;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final GraphTraversalSource g;

    public Optional<ProjectDTO> getProjectById(String id) {
        var projectVertex = g.V().has(ProjectSchema.ID, id).tryNext();
        return projectVertex.map(ProjectDTO::mapVertexToProjectDTO);
    }

    public ProjectDTO createProject(UpsertProjectReq request) {
        var id = UUID.randomUUID().toString();
        var projectVertex = g.addV(ProjectSchema.VERTEX_LABEL)
            .property(ProjectSchema.ID, id)
            .property(ProjectSchema.PROJECT_NAME, request.projectName())
            .property(ProjectSchema.BUDGET, request.budget())
            .property(ProjectSchema.START_DATE, request.startDate().toEpochDay())
            .property(ProjectSchema.END_DATE, request.endDate().toEpochDay())
            .next();

        g.tx().commit();
        return ProjectDTO.mapVertexToProjectDTO(projectVertex);
    }

    public boolean updateProject(String id, UpsertProjectReq request) {
        var projectVertexOpt = g.V().has(ProjectSchema.ID, id).tryNext();

        if (projectVertexOpt.isEmpty()) {
            return false;
        }

        var projectVertex = projectVertexOpt.get();
        projectVertex.property(ProjectSchema.PROJECT_NAME, request.projectName());
        projectVertex.property(ProjectSchema.BUDGET, request.budget());
        projectVertex.property(ProjectSchema.START_DATE, request.startDate().toEpochDay());
        projectVertex.property(ProjectSchema.END_DATE, request.endDate().toEpochDay());

        g.tx().commit();
        return true;
    }

    public boolean deleteProject(String id) {
        var projectVertexOpt = g.V().has(ProjectSchema.ID, id).tryNext();

        if (projectVertexOpt.isEmpty()) {
            return false;
        }

        projectVertexOpt.get().remove();
        g.tx().commit();
        return true;
    }

    public List<ProjectDTO> searchProjectsByName(String name) {
        return g.V().hasLabel(ProjectSchema.VERTEX_LABEL)
            .has(ProjectSchema.PROJECT_NAME, name)
            .toStream()
            .map(ProjectDTO::mapVertexToProjectDTO)
            .toList();
    }

    public List<ProjectDTO> getAllProjects() {
        return g.V().hasLabel(ProjectSchema.VERTEX_LABEL)
            .toStream()
            .map(ProjectDTO::mapVertexToProjectDTO)
            .toList();
    }

    public List<ProjectDTO> searchProjectsByDateRange(LocalDate startDate, LocalDate endDate) {
        return g.V().hasLabel(ProjectSchema.VERTEX_LABEL)
            .has(ProjectSchema.START_DATE, P.gte(startDate.toEpochDay()))
            .has(ProjectSchema.END_DATE, P.lte(endDate.toEpochDay()))
            .toStream()
            .map(ProjectDTO::mapVertexToProjectDTO)
            .toList();
    }

    public List<ProjectDTO> searchProjectsByBudgetRange(Double minBudget, Double maxBudget) {
        return g.V().hasLabel(ProjectSchema.VERTEX_LABEL)
            .has(ProjectSchema.BUDGET, P.between(minBudget, maxBudget))
            .toStream()
            .map(ProjectDTO::mapVertexToProjectDTO)
            .toList();
    }

    public List<EmployeeDTO> getEmployeesByProject(String projectId) {
        return g.V().has(ProjectSchema.ID, projectId)
            .in(EmployeeSchema.EDGE_WORKS_ON)
            .toStream()
            .map(EmployeeDTO::mapVertexToEmployeeDTO)
            .toList();
    }

}
