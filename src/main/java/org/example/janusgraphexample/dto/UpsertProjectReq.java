package org.example.janusgraphexample.dto;

import java.time.LocalDate;

public record UpsertProjectReq(String projectName, Double budget, LocalDate startDate, LocalDate endDate) {
}

