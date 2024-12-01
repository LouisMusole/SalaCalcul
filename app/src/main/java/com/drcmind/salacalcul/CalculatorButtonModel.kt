package com.drcmind.salacalcul

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector


sealed class CalculatorButtonModel(
    val text: String?,
    val icon: ImageVector? = null,
    val isAccent: Boolean = false){

    data class CalculatorButtonClear(
        val onClickClear: () -> EvaluationResult = { onClearAll() } ,
    ) : CalculatorButtonModel(text = "AC", isAccent = true)

    data class CalculatorButtonParenthesis(
        val onClickParenthesis: (String) -> EvaluationResult = { updateParenthesis(it) },
    ) : CalculatorButtonModel("( )", isAccent = true)

    data class CalculatorButtonOperator(
        val textOperator: String? = null,
        val onClickOperator: ((expression :String) -> String)? = null,
    ) : CalculatorButtonModel(textOperator, isAccent = true)

    data class CalculatorButtonNumber(
        val textNumber: String? = null,
        val onClickNumber: ((expression : String) -> EvaluationResult)? = null,
    ) : CalculatorButtonModel(textNumber)

    data class CalculatorButtonAppendPoint(
        val onClickPoint : (expression :String) -> String = { appendPoint(it) },
    ) : CalculatorButtonModel(text = ".")

    data class CalculatorButtonRemoveDigit(
        val backIcon : ImageVector = Icons.AutoMirrored.Default.ArrowBack,
        val onClickBack : ((expression :String) -> EvaluationResult) = { handleBackSpace(it) },
    ) : CalculatorButtonModel("", backIcon)

    data class CalculatorButtonEgal(
        val onClickEgal: ((expression : String) -> EvaluationResult) = { handleExpression(it) },
    ) : CalculatorButtonModel(text = "=", isAccent = true)

}


