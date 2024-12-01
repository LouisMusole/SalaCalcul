package com.drcmind.salacalcul

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

object CalculatorData {
    val buttons = listOf(
        listOf<CalculatorButtonModel>(
            CalculatorButtonModel.CalculatorButtonClear(),
            CalculatorButtonModel.CalculatorButtonParenthesis(),
            CalculatorButtonModel.CalculatorButtonOperator(
                textOperator = "^",
                onClickOperator = { expression->
                    appendOperatorToExpression(expression, '^')
                }
            ),
            CalculatorButtonModel.CalculatorButtonOperator(
                textOperator = "÷",
                onClickOperator = { expression->
                    appendOperatorToExpression(expression, '/')
                }
            )
        ),
        listOf(
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "7",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '7')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "8",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '8')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "9",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '9')
                }
            ),
            CalculatorButtonModel.CalculatorButtonOperator(
                textOperator = "X",
                onClickOperator = { expression->
                    appendOperatorToExpression(expression, '*')
                }
            )
        ),
        listOf(
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "4",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '4')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "5",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '5')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "6",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '6')
                }
            ),
            CalculatorButtonModel.CalculatorButtonOperator(
                textOperator = "-",
                onClickOperator = { expression->
                    appendOperatorToExpression(expression, '-')
                }
            )
        ),
        listOf(
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "1",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '1')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "2",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '2')
                }
            ),
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "3",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '3')
                }
            ),
            CalculatorButtonModel.CalculatorButtonOperator(
                textOperator = "+",
                onClickOperator = { expression->
                    appendOperatorToExpression(expression, '+')
                }
            )
        ),
        listOf(
            CalculatorButtonModel.CalculatorButtonNumber(
                textNumber = "0",
                onClickNumber = { expression->
                    appendDigitAndEvaluate(expression, '0')
                }
            ),
            CalculatorButtonModel.CalculatorButtonAppendPoint(
                onClickPoint = { expression->
                    appendPoint(expression)
                }
            ),
            CalculatorButtonModel.CalculatorButtonRemoveDigit(
                backIcon = Icons.AutoMirrored.Default.ArrowBack,
                onClickBack = { expression->
                    handleBackSpace(expression)
                }
            ),
            CalculatorButtonModel.CalculatorButtonEgal(
                onClickEgal = { expression->
                    handleExpression(expression)
                }
            )
        )
    )
}

fun appendPoint(expression: String) : String{
    val condition = !expression.substringAfterLast("+").contains(".") ||
            !expression.substringAfterLast("-").contains(".") ||
            !expression.substringAfterLast("/").contains(".") ||
            !expression.substringAfterLast("*").contains(".") ||
            !expression.substringAfterLast("^").contains(".")

    return if(expression.isNotEmpty()){
        if(condition && expression.last() !in setOf('+', '-', '/', '*', '^')){
            "$expression."
        }else{
            expression
        }
    }else{
        expression
    }
}

fun handleBackSpace(expression: String) : EvaluationResult{
    val updatedExpression = expression.dropLast(1)
    return EvaluationResult(updatedExpression, try{formatDouble(CalculatorEvaluateExpression.evaluate(updatedExpression))}catch (e : Exception){""})
}

fun handleExpression(expression: String) : EvaluationResult {
    return EvaluationResult(formatDouble(CalculatorEvaluateExpression.evaluate(expression)), "")
}

fun formatDouble(value: Double): String {
    return if (value % 1 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}


fun appendDigitAndEvaluate(expression: String, digit: Char): EvaluationResult {
    val updatedExpression = expression + digit

    return if (isEvaluable(updatedExpression)) {
        try {
            val result = formatDouble(CalculatorEvaluateExpression.evaluate(updatedExpression))
            EvaluationResult(updatedExpression, result) // No error
        } catch (e: Exception) {
            EvaluationResult(updatedExpression, error = e.message) // Store error message
        }
    } else {
        EvaluationResult(updatedExpression) // No error, result is empty by default
    }
}

private fun isEvaluable(expression: String): Boolean {
    val operators = setOf('+', '-', '/', '*', '^')
    return expression.any { it in operators } && expression.count { it == '(' } % 2 == 0
}

fun appendOperatorToExpression(expression: String, operator: Char): String {
    return if (expression.isEmpty()) {
        expression // Do not append if expression is empty or ends with an operator
    } else if(expression.last() in setOf('+', '-', '/', '*', '^')) {
        expression.dropLast(1) + operator // Append the operator
    }else{
        expression + operator // Append the operator
    }
}

fun updateParenthesis(expression : String) : EvaluationResult{

    return if (shouldAddOpeningParenthesis(expression)) {
        EvaluationResult("$expression(")
    } else if (shouldAddClosingParenthesis(expression)) {
        try {
            EvaluationResult("$expression)",formatDouble(CalculatorEvaluateExpression.evaluate("$expression)")))
        } catch (e: Exception) {
            EvaluationResult("$expression)", error = e.message)  // Or handle the exception appropriately
        }
    } else {
        EvaluationResult(expression)
    }
}

fun shouldAddOpeningParenthesis(expression: String): Boolean {
    return expression.isEmpty() || (expression.last() in setOf('+', '-', '/', '*', '^') && expression.count { it == '(' } % 2 == 0)
}

fun shouldAddClosingParenthesis(expression: String): Boolean {
    return expression.count { it == '(' } % 2 != 0
}

fun onClearAll() = EvaluationResult("")