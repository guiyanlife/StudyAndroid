package com.github.studyandroid.app.domain.model

/**
 * Domain 层数据模型 - 纯 Kotlin 数据类，不依赖任何框架
 * 与 Room Entity、Network DTO 完全解耦，通过 Mapper 互转
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: String,
    val companySlogan: String,
    val companyBs: String,
    val avatarUrl: String
)
