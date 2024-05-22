package org.example.janusgraphexample.dto;

import java.util.List;
import java.util.Set;

public record UpsertEmployeeReq(String firstName, String lastName, String email, Set<String> skills,
                                List<String> projectIds) {
}
