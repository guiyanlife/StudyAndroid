package com.github.studyandroid.app.domain.usecase

import com.github.studyandroid.app.domain.repository.UserRepository
import javax.inject.Inject

/**
 * UseCase：从网络刷新用户列表并写入本地数据库
 *
 * suspend 函数，由调用方在协程中执行。
 */
class RefreshUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.refreshUsers()
}
