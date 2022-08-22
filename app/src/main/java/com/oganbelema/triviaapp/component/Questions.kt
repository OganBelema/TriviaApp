package com.oganbelema.triviaapp.component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oganbelema.triviaapp.util.AppColors
import com.oganbelema.triviaapp.viewmodel.QuestionViewModel

@Composable
fun Questions(modifier: Modifier = Modifier,
    viewModel: QuestionViewModel){
    val questions = viewModel.dataOrException.value.data

    if (viewModel.dataOrException.value.isLoading == true) {

        CircularProgressIndicator(modifier)

        Log.d("Loading", "Loading.....")
    } else {
        Log.d("Loading", "Loading Stopped.....")

        questions?.get(0).let { question ->
            Log.d("Result", "Question: ${question?.question}")
        }
    }
}

@Preview
@Composable
fun QuestionDisplay() {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(4.dp),
    color = AppColors.darkPurple) {
        Column(modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            QuestionTracker()
        }
    }
}


@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.lightGray,
                fontWeight = FontWeight.Bold, fontSize = 28.sp)){
                append("Question $counter/")
                withStyle(style = SpanStyle(
                    color = AppColors.lightGray, fontWeight = FontWeight.Light, fontSize = 14.sp
                )
                ) {
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}