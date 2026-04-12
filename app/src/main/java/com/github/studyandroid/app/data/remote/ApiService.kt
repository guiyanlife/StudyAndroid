package com.github.studyandroid.app.data.remote

import com.github.studyandroid.app.data.remote.dto.UserDto
import retrofit2.http.GET

/**
 * Retrofit API 接口
 * suspend 函数配合协程，无需 Callback
 */
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
