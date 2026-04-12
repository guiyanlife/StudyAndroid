package com.github.studyandroid.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity - 数据库表结构
 * 与 Domain Model 解耦，通过 Mapper 转换
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val companyName: String,
    val companySlogan: String,
    val companyBs: String,
    val avatarUrl: String
)
