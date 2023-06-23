package com.nextar.calculator.repositories;

import com.nextar.calculator.models.CalculatorRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface RequestRepository extends JpaRepository<CalculatorRequest,Long> {
    CalculatorRequest findByExpression(String expression);

}
