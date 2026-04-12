package com.github.studyandroid.app.data.repository

import com.github.studyandroid.app.data.local.dao.UserDao
import com.github.studyandroid.app.data.mapper.toDomain
import com.github.studyandroid.app.data.mapper.toEntity
import com.github.studyandroid.app.data.remote.ApiService
import com.github.studyandroid.app.domain.model.User
import com.github.studyandroid.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository 实现 - Offline-First 策略
 *
 * 数据流：
 *   UI 订阅 Room Flow（实时响应）
 *      ↑
 *   refreshUsers() 从网络拉取 → 写入 Room
 *      ↑
 *   Room 变化自动通过 Flow 推送给 UI（无需手动通知）
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService
) : UserRepository {
    override fun getUsers(): Flow<List<User>> =
        userDao.getUsers().map { it.map { entity -> entity.toDomain() } }

    override fun getUserById(id: Int): Flow<User?> =
        userDao.getUserById(id).map { it?.toDomain() }

    override suspend fun refreshUsers() {
        val entities = apiService.getUsers().map { it.toEntity() }
        userDao.replaceAll(entities)
    }
}
