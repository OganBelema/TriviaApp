package com.oganbelema.triviaapp.screen

import androidx.compose.runtime.Composable
import com.oganbelema.triviaapp.component.Questions
import com.oganbelema.triviaapp.viewmodel.QuestionViewModel

@Composable
fun TriviaHome(viewModel: QuestionViewModel) {
    Questions(viewModel = viewModel)
}