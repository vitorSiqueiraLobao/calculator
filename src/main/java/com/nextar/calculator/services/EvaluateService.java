package com.nextar.calculator.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Service
public class EvaluateService {
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
            double ineerResult;
            if (expressionWithoutDecimals.charAt(operatorIndex) == '*') {
                ineerResult = leftOperand * rightOperand;
            } else {
                ineerResult = leftOperand / rightOperand;
            }
            expression = expression.replace(operands[operatorIndex-1] + operands[operatorIndex] + operands[operatorIndex+1], String.valueOf(ineerResult));
        }

        // Realiza adição e subtração
        while (expression.contains("-")||expression.contains("+")){
            String expressionWithoutDecimals = expression.replaceAll("\\.\\d+", "");
            String[] operands = expression.split("(?<=[-+*/])|(?=[-+*/])");

            int subIndex = Arrays.asList(operands).indexOf("-");
            int addIndex = Arrays.asList(operands).indexOf("+");
            int operatorIndex;
            if (subIndex >= 0 && addIndex >= 0) {
                operatorIndex = Math.min(subIndex, addIndex);
            } else if (subIndex >= 0) {
                operatorIndex = subIndex;
            } else {
                operatorIndex = addIndex;
            }

            double leftOperand = Double.parseDouble(operands[operatorIndex-1]);
            double rightOperand = Double.parseDouble(operands[operatorIndex+1]);
            double ineerResult;
            System.out.println(operands[operatorIndex].equals('+'));
            if (operands[operatorIndex].equals('+')) {
                ineerResult = leftOperand + rightOperand;
            } else {
                ineerResult = leftOperand - rightOperand;
            }
            expression = expression.replace(operands[operatorIndex-1] + operands[operatorIndex] + operands[operatorIndex+1], String.valueOf(ineerResult));
        }

        double result =Double.parseDouble(expression);
        BigDecimal bd = BigDecimal.valueOf(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
