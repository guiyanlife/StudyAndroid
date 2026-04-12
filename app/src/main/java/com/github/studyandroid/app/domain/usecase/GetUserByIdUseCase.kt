package com.github.studyandroid.app.domain.usecase

import com.github.studyandroid.app.domain.model.User
import com.github.studyandroid.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase：根据 ID 获取单个用户（持续观察数据库变化）
 */
class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: Int): Flow<User?> = repository.getUserById(userId)
}
