package com.nextar.calculator.controllers;

import com.nextar.calculator.classes.CalculatorError;
import com.nextar.calculator.classes.CalculatorParameters;
import com.nextar.calculator.classes.CalculatorRequest;
import com.nextar.calculator.classes.CalculatorResult;
import com.nextar.calculator.repositories.RequestRepository;
import com.nextar.calculator.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
class CalculatorController {

    @Autowired
    private RequestService requestService;

    @PostMapping("/calculate")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> CalculateExpression(@RequestBody CalculatorParameters parameters){
        try{
            String expression = parameters.getExpressao();


            expression = expression.replaceAll("\\s+", "");//remove espaços em branco

            if (!expression.matches("[\\d+\\-*/().]+")) {
                throw new IllegalArgumentException("Invalid expression");
            }
            Double evaluateResult = evaluate(expression);
            CalculatorResult result = new CalculatorResult(evaluateResult);

            CalculatorRequest calcRequest = new CalculatorRequest();
            calcRequest.setExpression(expression);
            calcRequest.setResult(evaluateResult);
            requestService.saveRequest(calcRequest);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CalculatorError(e.getMessage()));
        }

    }

    public double evaluate(String expression){
        while (expression.contains("(")) {
            int openParenIndex = expression.lastIndexOf("(");
            int closeParenIndex = expression.indexOf(")", openParenIndex);
            String subExpression = expression.substring(openParenIndex + 1, closeParenIndex);
            double subResult = evaluate(subExpression);
            expression = expression.replace("(" + subExpression + ")", String.valueOf(subResult));
        }

        // Realiza multiplicação e divisão
        while (expression.contains("*") || expression.contains("/")) {
            String expressionWithoutDecimals = expression.replaceAll("\\.\\d+", "");
            int multiplyIndex = expressionWithoutDecimals.indexOf("*");
            int divideIndex = expressionWithoutDecimals.indexOf("/");
            int operatorIndex;
            if (multiplyIndex >= 0 && divideIndex >= 0) {
                operatorIndex = Math.min(multiplyIndex, divideIndex);
            } else if (multiplyIndex >= 0) {
                operatorIndex = multiplyIndex;
            } else {
                operatorIndex = divideIndex;
            }

            String[] operands = expression.split("(?<=[-+*/])|(?=[-+*/])");
            double leftOperand = Double.parseDouble(operands[operatorIndex-1]);
            double rightOperand = Double.parseDouble(operands[operatorIndex+1]);
            double result;
            if (expressionWithoutDecimals.charAt(operatorIndex) == '*') {
                result = leftOperand * rightOperand;
            } else {
                result = leftOperand / rightOperand;
            }
            expression = expression.replace(operands[operatorIndex-1] + operands[operatorIndex] + operands[operatorIndex+1], String.valueOf(result));
        }

        // Realiza adição e subtração
        double result = 0;
        while (expression.contains("-")||expression.contains("+")){
            String expressionWithoutDecimals = expression.replaceAll("\\.\\d+", "");
            int subIndex = expressionWithoutDecimals.indexOf("-");
            int addIndex = expressionWithoutDecimals.indexOf("+");
            int operatorIndex;
            if (subIndex >= 0 && addIndex >= 0) {
                operatorIndex = Math.min(subIndex, addIndex);
            } else if (subIndex >= 0) {
                operatorIndex = subIndex;
            } else {
                operatorIndex = addIndex;
            }
            String[] operands = expression.split("(?<=[-+*/])|(?=[-+*/])");
            double leftOperand = Double.parseDouble(operands[operatorIndex-1]);
            double rightOperand = Double.parseDouble(operands[operatorIndex+1]);
            if (expressionWithoutDecimals.charAt(operatorIndex) == '+') {
                result = leftOperand + rightOperand;
            } else {
                result = leftOperand - rightOperand;
            }
            expression = expression.replace(operands[operatorIndex-1] + operands[operatorIndex] + operands[operatorIndex+1], String.valueOf(result));
        }

        return result;
    }

}
