package com.app.restapiapp.retrofit

import com.app.restapiapp.models.User
import com.app.restapiapp.models.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsers(@Query("page") page: Int): Call<Users>

    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: Int) : Call<User>
}