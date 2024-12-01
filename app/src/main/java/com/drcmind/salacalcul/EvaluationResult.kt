package com.drcmind.salacalcul

data class EvaluationResult(
    val expression: String,
    val result: String = "",
    val error: String? = null
)
