package com.oganbelema.triviaapp.data

data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var isLoading: Boolean? = null,
    var error: E? = null
)