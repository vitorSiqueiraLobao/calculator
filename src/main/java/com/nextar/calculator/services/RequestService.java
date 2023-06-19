package com.nextar.calculator.services;

import com.nextar.calculator.classes.CalculatorRequest;
import com.nextar.calculator.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public CalculatorRequest findByExpression(String expression){
        return  requestRepository.findByExpression(expression);
    }

    public CalculatorRequest saveRequest(CalculatorRequest request) {
        return requestRepository.save(request);
    }
}
