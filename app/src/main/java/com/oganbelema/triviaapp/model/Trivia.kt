package com.oganbelema.triviaapp.model

data class Trivia(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)