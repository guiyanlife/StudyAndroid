package com.github.studyandroid.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO - 与 API 响应结构完全对应
 * 使用 @Serializable 做 Kotlin Serialization（替代 Gson/Moshi）
 */
@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: CompanyDto,
    val avatarUrl: String
)

@Serializable
data class CompanyDto(
    val name: String,
    val slogan: String = "",
    val bs: String = ""
)
