package com.drcmind.salacalcul

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.text.dropLast
import kotlin.text.isEmpty
import kotlin.text.last

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainPaneScreen(
    onNavigateToSupportingPane : ()->Unit,
    onAddToHistory : (String)->Unit,
    isSupportingPaneHidden : Boolean
){
    var evaluationResult by remember { mutableStateOf(EvaluationResult("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if(isSupportingPaneHidden){
                        IconButton(
                            onClick = {
                                onNavigateToSupportingPane()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.history),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = TextFieldValue(evaluationResult.expression, TextRange(evaluationResult.expression.length)),
                    onValueChange = {},
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Right,
                        fontSize = 42.sp
                    ),
                    isError = evaluationResult.error != null,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(showKeyboardOnFocus = false)
                )
                TextField(
                    value = evaluationResult.result,
                    onValueChange = {},
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Right,
                        fontSize = 24.sp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 1
                )
            }

        Column(modifier = Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CalculatorData.buttons.forEach {rowList->
                    Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowList.forEach {button->
                            SalaCalculButton(
                                modifier = Modifier.weight(1f),
                                text = button.text.toString(),
                                isAccent = button.isAccent,
                                icon = button.icon,
                                onClick = {
                                    when(button){
                                        is CalculatorButtonModel.CalculatorButtonAppendPoint -> {
                                            evaluationResult = try {
                                                evaluationResult.copy(expression = button.onClickPoint(evaluationResult.expression), error = null)
                                            }catch (e : Exception){
                                                evaluationResult.copy(expression = button.onClickPoint(evaluationResult.expression), error = e.message)
                                            }
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonClear -> {
                                            evaluationResult = button.onClickClear()
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonEgal -> {
                                            evaluationResult = try {
                                                if(evaluationResult.result.isNotEmpty()){
                                                    onAddToHistory(evaluationResult.expression+" = "+evaluationResult.result)
                                                    button.onClickEgal (evaluationResult.expression)
                                                }else evaluationResult
                                            }catch (e : Exception){
                                                evaluationResult.copy(error = e.message)
                                            }
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonNumber -> {
                                            evaluationResult = try {
                                                button.onClickNumber!!(evaluationResult.expression)
                                            }catch (e : Exception){
                                                evaluationResult.copy(error = e.message)
                                            }
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonOperator -> {
                                            evaluationResult =evaluationResult.copy(expression = button.onClickOperator!!(evaluationResult.expression))
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonParenthesis -> {
                                            evaluationResult = try {
                                                button.onClickParenthesis(evaluationResult.expression)
                                            }catch (e : Exception){
                                                evaluationResult.copy(error = e.message)
                                            }
                                            return@SalaCalculButton
                                        }
                                        is CalculatorButtonModel.CalculatorButtonRemoveDigit -> {
                                            evaluationResult = try {
                                                button.onClickBack(evaluationResult.expression)
                                            } catch (e : Exception){
                                                evaluationResult.copy(error = e.message)
                                            }
                                            return@SalaCalculButton
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun SalaCalculButton(
    modifier: Modifier = Modifier,
    text: String = "",
    icon: ImageVector? = null,
    isAccent: Boolean = false,
    onClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val buttonColors = if (isAccent) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.filledTonalButtonColors()
        }
        Button(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.fillMaxSize(),
            colors = buttonColors,
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = text, fontSize = 27.sp)
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "Description of the icon")
            }
        }
    }
}

@Preview
@Composable
fun MainPaneScreenPreview(){
    MainPaneScreen({},{}, false)
}
