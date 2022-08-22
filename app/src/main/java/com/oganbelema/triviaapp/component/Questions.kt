package com.oganbelema.triviaapp.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oganbelema.triviaapp.model.Question
import com.oganbelema.triviaapp.util.AppColors
import com.oganbelema.triviaapp.viewmodel.QuestionViewModel

@Composable
fun Questions(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel
) {
    val questions = viewModel.dataOrException.value.data

    if (viewModel.dataOrException.value.isLoading == true) {

        CircularProgressIndicator(modifier)

        Log.d("Loading", "Loading.....")
    } else {
        Log.d("Loading", "Loading Stopped.....")

        questions?.get(0)?.let { question ->
            QuestionDisplay(question = question)
        }
    }
}


@Composable
fun QuestionDisplay(
    question: Question,
    //questionIndex: MutableState<Int>,
    //viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit = {}
) {
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.darkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            DrawDottedLine(
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            Column {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.offWhite
                )

                //choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.offDarkPurple,
                                        AppColors.offDarkPurple
                                    )
                                ), shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(50))
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = (answerState.value == index), onClick = {
                                updateAnswer(index)
                            }, modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                if (correctAnswerState.value == true && index == answerState.value)
                                    Color.Green.copy(alpha = 0.2f)
                                else
                                    Color.Red.copy(alpha = 0.2f)
                            )
                        )

                        Text(text = answerText)
                    }
                }
            }
        }
    }
}


@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.lightGray,
                    fontWeight = FontWeight.Bold, fontSize = 28.sp
                )
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.lightGray, fontWeight = FontWeight.Light, fontSize = 14.sp
                    )
                ) {
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp),
        onDraw = {
            drawLine(
                color = AppColors.lightGray, start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                pathEffect = pathEffect
            )
        })
}