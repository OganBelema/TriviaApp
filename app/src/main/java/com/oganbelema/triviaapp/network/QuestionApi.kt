package com.oganbelema.triviaapp.network

import com.oganbelema.triviaapp.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("music.json")
    suspend fun getMusicQuestions(): List<Question>
}