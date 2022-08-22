package com.oganbelema.triviaapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oganbelema.triviaapp.data.DataOrException
import com.oganbelema.triviaapp.model.Question
import com.oganbelema.triviaapp.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val repository: QuestionRepository) : ViewModel() {

    private val _dataOrException: MutableState<DataOrException<List<Question>,
            Boolean, Exception>> = mutableStateOf(DataOrException())

    val dataOrException: State<DataOrException<List<Question>,
            Boolean, Exception>> get() = _dataOrException

    init {
        getMovieQuestions()
    }

    private fun getMovieQuestions() {
        viewModelScope.launch {
            _dataOrException.value.isLoading = true
            _dataOrException.value = repository.getMusicQuestions()
            if (_dataOrException.value.data != null) _dataOrException.value.isLoading = false
        }
    }

}