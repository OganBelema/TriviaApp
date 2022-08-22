package com.oganbelema.triviaapp.network

import com.oganbelema.triviaapp.model.Trivia
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("music.json")
    suspend fun getMusicQuestions(): List<Trivia>
}