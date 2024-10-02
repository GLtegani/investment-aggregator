package com.devtegani_study.investimentsaggregator.exceptions.exceptionHandler;

import com.devtegani_study.investimentsaggregator.InvestimentsaggregatorApplication;
import com.devtegani_study.investimentsaggregator.exceptions.InvestmentAggregatorException;
import com.devtegani_study.investimentsaggregator.exceptions.UserAlreadyExistException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvestmentAggregatorExceptionHandler {
    @ExceptionHandler(InvestmentAggregatorException.class)
    public final ProblemDetail handleInvestmentAggregatorException(InvestmentAggregatorException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public final ProblemDetail handleUserAlreadyExistException(UserAlreadyExistException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(e.getMessage());
        return problemDetail;
    }
}
