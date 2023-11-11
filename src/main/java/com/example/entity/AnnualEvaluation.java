package com.example.entity;

import lombok.Data;

import java.time.Year;

@Data
public class AnnualEvaluation {
    private int evaluationID;
    private int employeeID;
    private Year year;
    private int score;
    private String feedback;
}
