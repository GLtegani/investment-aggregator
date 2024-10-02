package com.devtegani_study.investimentsaggregator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserAlreadyExistException extends InvestmentAggregatorException{
    @Override
    public final ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("User already exist");
        return problemDetail;
    }
}
