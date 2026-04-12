package com.github.studyandroid.app.domain.repository

import com.github.studyandroid.app.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository 接口 - 依赖倒置原则
 * 上层（ViewModel/UseCase）只依赖此接口，不依赖具体实现
 */
interface UserRepository {
    /** Offline-First：先返回数据库缓存，网络刷新后自动更新 */
    fun getUsers(): Flow<List<User>>
    fun getUserById(id: Int): Flow<User?>
    suspend fun refreshUsers()
}