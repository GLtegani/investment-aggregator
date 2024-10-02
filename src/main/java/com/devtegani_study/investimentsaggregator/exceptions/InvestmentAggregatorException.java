package com.devtegani_study.investimentsaggregator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class InvestmentAggregatorException extends RuntimeException{
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Investment Aggregator internal server error");
        return problemDetail;
    }
}
