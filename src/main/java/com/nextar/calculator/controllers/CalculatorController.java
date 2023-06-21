package com.nextar.calculator.controllers;

import com.nextar.calculator.classes.CalculatorError;
import com.nextar.calculator.classes.CalculatorParameters;
import com.nextar.calculator.classes.CalculatorRequest;
import com.nextar.calculator.classes.CalculatorResult;
import com.nextar.calculator.repositories.RequestRepository;
import com.nextar.calculator.services.EvaluateService;
import com.nextar.calculator.services.RequestService;
import com.oracle.truffle.js.runtime.objects.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
class CalculatorController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private EvaluateService evaluateService;

    @PostMapping("/calculate")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> CalculateExpression(@RequestBody CalculatorParameters parameters){
        try{
            String expression = parameters.getExpressao();
            CalculatorRequest resultFromDatabase = requestService.findByExpression(expression);
            CalculatorResult result = new CalculatorResult();
            if(resultFromDatabase != null){
                result.setResultado(resultFromDatabase.getResult());
            }else{
                expression = expression.replaceAll("\\s+", "");//remove espaços em branco

                //confirma que não existe nenhum caracter invalido
                if (!expression.matches("[\\d+\\-*/().]+")) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                Double evaluateResult = evaluateService.evaluate(expression);
                result = new CalculatorResult(evaluateResult);

                CalculatorRequest calcRequest = new CalculatorRequest();
                calcRequest.setExpression(expression);
                calcRequest.setResult(evaluateResult);
                requestService.saveRequest(calcRequest);
            }





            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CalculatorError(e.getMessage()));
        }

    }



}
