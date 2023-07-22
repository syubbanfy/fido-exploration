package com.digiventure.fido_exploration.api

import com.digiventure.fido_exploration.model.Option
import com.digiventure.fido_exploration.model.RegisterCredentialBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthAPI {
    @Headers("Content-Type: application/json")
    @GET("register")
    suspend fun getOption(@Header("Authorization") bearerToken: String): Option

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun registerCredential(@Body registerCredentialBody: RegisterCredentialBody)
}