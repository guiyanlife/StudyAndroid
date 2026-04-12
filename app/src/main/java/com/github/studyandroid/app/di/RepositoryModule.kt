package com.github.studyandroid.app.di

import com.github.studyandroid.app.domain.repository.UserRepository
import com.github.studyandroid.app.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 将接口绑定到实现类
 * @Binds 比 @Provides 更高效（无额外实例化）
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
