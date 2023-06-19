package com.nextar.calculator.repositories;

import com.nextar.calculator.classes.CalculatorRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface RequestRepository extends JpaRepository<CalculatorRequest,Long> {
}
