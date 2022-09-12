package com.quanvu201120.supportfit.service

import com.quanvu201120.supportfit.model.BodyApi
import com.quanvu201120.supportfit.model.ResultApiModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ICallApi {
    @POST("/fcm/send")
    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAALqgZFPY:APA91bH9xAenumXalXmJUq86PFEwFp26el6_ZUqLdJmJKN0XOQzkvRH2zj8gQoAU2yNraQigBYTcZ6EOiScj2fhDTyLMg_B7yC4Mv2XbzLLEdfFYeUKBxB2ZTyawYGiBH4wSXCeEZqvZ"
    )
    fun sendNotifyApi(@Body body : BodyApi) : retrofit2.Call<ResultApiModel>
}