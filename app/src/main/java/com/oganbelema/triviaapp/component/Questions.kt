package com.oganbelema.triviaapp.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oganbelema.triviaapp.R
import com.oganbelema.triviaapp.model.Question
import com.oganbelema.triviaapp.util.AppColors
import com.oganbelema.triviaapp.viewmodel.QuestionViewModel

@Composable
fun Questions(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel
) {
    val questions = viewModel.dataOrException.value.data

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (viewModel.dataOrException.value.isLoading == true) {

        Column(
            modifier = modifier.fillMaxSize()
                .background(color = AppColors.darkPurple),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = modifier)
        }

    } else {
        val question = try {
            questions?.get(index = questionIndex.value)
        } catch (exception: Exception) {
            null
        }

        if (question != null) {
            QuestionDisplay(
                question = question, questionIndex = questionIndex,
                viewModel = viewModel
            ) {
                questionIndex.value += 1
            }
        }
    }
}


@Composable
fun QuestionDisplay(
    question: Question,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel,
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

            if (questionIndex.value >= 3) ShowProgress(score = questionIndex.value)

            QuestionTracker(
                counter = questionIndex.value,
                outOf = viewModel.dataOrException.value.data?.size ?: 0
            )

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
                    QuestionChoiceRow {
                        QuestionChoiceRadioButton(
                            answerState,
                            index,
                            updateAnswer,
                            correctAnswerState
                        )

                        QuestionChoiceText(
                            correctAnswerState,
                            index,
                            answerState,
                            answerText
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = buttonColors(
                        backgroundColor = AppColors.lightBlue
                    ),
                    onClick = {
                        onNextClicked.invoke(questionIndex.value)
                    }) {
                    Text(
                        text = stringResource(id = R.string.next),
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.offWhite,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionChoiceRow(
    content: @Composable () -> Unit
) {
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
        content()
    }
}

@Composable
private fun QuestionChoiceRadioButton(
    answerState: MutableState<Int?>,
    index: Int,
    updateAnswer: (Int) -> Unit,
    correctAnswerState: MutableState<Boolean?>
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
}

@Composable
private fun QuestionChoiceText(
    correctAnswerState: MutableState<Boolean?>,
    index: Int,
    answerState: MutableState<Int?>,
    answerText: String
) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Light,
                color = if (correctAnswerState.value == true &&
                    index == answerState.value
                ) {
                    Color.Green
                } else if (correctAnswerState.value == false &&
                    index == answerState.value
                ) {
                    Color.Red
                } else {
                    AppColors.offWhite
                }, fontSize = 18.sp
            )
        ) {
            append(answerText)
        }
    })
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

@Preview
@Composable
fun ShowProgress(score: Int = 12) {

    val progressFactor = remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp, brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.lightPurple, AppColors.lightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(RoundedCornerShape(50))
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFF95075),
                            Color(0xFFBE6BE5)
                        )
                    )
                ),
            contentPadding = PaddingValues(1.dp),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            ),
            onClick = {}) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(24.dp))
                    .fillMaxHeight(0.86f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.offWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}