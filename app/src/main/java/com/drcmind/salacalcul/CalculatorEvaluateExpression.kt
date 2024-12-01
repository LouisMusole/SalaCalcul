package com.drcmind.salacalcul


object CalculatorEvaluateExpression {

    private var currentIndex = 0
    private lateinit var tokens: List<String>

    fun evaluate(expression: String): Double {
        // Tokenize the expression
        tokens = tokenize(expression.replace("\\s+".toRegex(), ""))  // Remove whitespaces
        currentIndex = 0
        return parseExpression()
    }

    private fun tokenize(expression: String): List<String> {
        val regex = """(\d+\.\d+|\d+|\+|\-|\*|\/|\^|\(|\))""".toRegex()
        return regex.findAll(expression).map { it.value }.toList()
    }

    // Parse the full expression (addition and subtraction)
    private fun parseExpression(): Double {
        var result = parseTerm()

        while (currentIndex < tokens.size) {
            val operator = tokens[currentIndex]
            if (operator == "+" || operator == "-") {
                currentIndex++
                val right = parseTerm()
                result = if (operator == "+") result + right else result - right
            } else {
                break
            }
        }

        return result
    }

    // Parse terms (multiplication, division, and exponentiation)
    private fun parseTerm(): Double {
        var result = parseFactor()

        while (currentIndex < tokens.size) {
            val operator = tokens[currentIndex]
            if (operator == "*" || operator == "/" || operator == "^") {
                currentIndex++
                val right = parseFactor()
                result = when (operator) {
                    "*" -> result * right
                    "/" -> result / right
                    "^" -> Math.pow(result, right)  // Use Math.pow for exponentiation
                    else -> result
                }
            } else {
                break
            }
        }

        return result
    }

    // Parse individual factors (numbers or sub-expressions in parentheses)
    private fun parseFactor(): Double {
        val token = tokens[currentIndex]

        return when {
            token == "(" -> {
                currentIndex++ // Skip '('
                val result = parseExpression() // Evaluate the sub-expression
                if (tokens[currentIndex] == ")") {
                    currentIndex++ // Skip ')'
                    result
                } else {
                    throw IllegalArgumentException("Expected closing parenthesis.")
                }
            }
            token.matches("""\d+(\.\d+)?""".toRegex()) -> {
                currentIndex++
                token.toDouble() // Number
            }
            else -> throw IllegalArgumentException("Unexpected token: $token")
        }
    }
}
