package org.example.janusgraphexample.dto;

import java.time.LocalDate;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.example.janusgraphexample.schemas.ProjectSchema;

public record ProjectDTO(String id, String projectName, Double budget, LocalDate startDate, LocalDate endDate) {

    public static ProjectDTO mapVertexToProjectDTO(Vertex projectVertex) {
        var id = projectVertex.property(ProjectSchema.ID).value().toString();
        var projectName = projectVertex.property(ProjectSchema.PROJECT_NAME).value().toString();
        var budget = (Double) projectVertex.property(ProjectSchema.BUDGET).value();

        var startDate = LocalDate.ofEpochDay((long) projectVertex.property(ProjectSchema.START_DATE).value());
        var endDate = LocalDate.ofEpochDay((long) projectVertex.property(ProjectSchema.END_DATE).value());

        return new ProjectDTO(id, projectName, budget, startDate, endDate);
    }
}
