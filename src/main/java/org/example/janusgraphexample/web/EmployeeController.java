package org.example.janusgraphexample.web;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.example.janusgraphexample.dto.EmployeeDTO;
import org.example.janusgraphexample.dto.UpsertEmployeeReq;
import org.example.janusgraphexample.service.EmployeeService;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id) {
        var employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody UpsertEmployeeReq upsertEmployeeReq) {
        var createdEmployee = employeeService.createEmployee(upsertEmployeeReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String id,
                                               @RequestBody UpsertEmployeeReq createEmployeeReq) {
        var isUpdated = employeeService.updateEmployee(id, createEmployeeReq);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        var isDeleted = employeeService.deleteEmployee(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Set<EmployeeDTO>> searchEmployeesBySkill(@RequestParam String skill) {
        var employees = employeeService.searchEmployeesBySkill(skill);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<EmployeeDTO>> createMultipleEmployees(@RequestBody List<UpsertEmployeeReq> employees) {
        var createdEmployees = employeeService.createMultipleEmployees(employees);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployees);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        var employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> assignEmployeeToProject(@PathVariable String employeeId,
                                                        @PathVariable String projectId) {
        var isAssigned = employeeService.assignEmployeeToProject(employeeId, projectId);
        if (isAssigned) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
