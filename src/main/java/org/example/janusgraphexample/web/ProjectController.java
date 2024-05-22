package org.example.janusgraphexample.web;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.janusgraphexample.dto.EmployeeDTO;
import org.example.janusgraphexample.dto.ProjectDTO;
import org.example.janusgraphexample.dto.UpsertProjectReq;
import org.example.janusgraphexample.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String id) {
        return projectService.getProjectById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody UpsertProjectReq request) {
        var createdProject = projectService.createProject(request);
        return ResponseEntity.ok(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable String id, @RequestBody UpsertProjectReq request) {
        boolean updated = projectService.updateProject(id, request);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/searchByName")
    public ResponseEntity<List<ProjectDTO>> searchProjectsByName(@RequestParam String name) {
        var projects = projectService.searchProjectsByName(name);
        return ResponseEntity.ok(projects);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        var projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/searchByDateRange")
    public ResponseEntity<List<ProjectDTO>> searchProjectsByDateRange(@RequestParam String startDate,
                                                                      @RequestParam String endDate) {
        var start = LocalDate.parse(startDate);
        var end = LocalDate.parse(endDate);
        var projects = projectService.searchProjectsByDateRange(start, end);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/searchByBudgetRange")
    public ResponseEntity<List<ProjectDTO>> searchProjectsByBudgetRange(@RequestParam Double minBudget,
                                                                        @RequestParam Double maxBudget) {
        var projects = projectService.searchProjectsByBudgetRange(minBudget, maxBudget);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByProject(@PathVariable String projectId) {
        var employees = projectService.getEmployeesByProject(projectId);
        return ResponseEntity.ok(employees);
    }
}
