package com.oganbelema.triviaapp.repository

import com.oganbelema.triviaapp.data.DataOrException
import com.oganbelema.triviaapp.model.Question
import com.oganbelema.triviaapp.network.QuestionApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(private val questionApi: QuestionApi) {

    private val dataOrException = DataOrException<List<Question>, Boolean, Exception>()

    suspend fun getMusicQuestions(): DataOrException<List<Question>, Boolean, Exception> {
        try {
            dataOrException.isLoading = true
            dataOrException.data = questionApi.getMusicQuestions()
            if (dataOrException.data != null) dataOrException.isLoading = false
        } catch (exception: Exception) {
            dataOrException.error = exception
            exception.printStackTrace()
        }

        return dataOrException
    }
}