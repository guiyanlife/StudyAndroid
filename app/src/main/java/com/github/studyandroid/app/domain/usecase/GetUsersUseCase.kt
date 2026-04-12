package com.github.studyandroid.app.domain.usecase

import com.github.studyandroid.app.domain.model.User
import com.github.studyandroid.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase：获取用户列表（持续观察数据库变化）
 *
 * 遵循单一职责原则，ViewModel 只需调用 invoke()。
 * 未来如需过滤/排序/分页逻辑，只改此处即可，不影响 ViewModel。
 */
class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.getUsers()
}
