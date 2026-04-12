package com.github.studyandroid.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.studyandroid.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO - 数据访问对象
 * 返回 Flow 实现响应式数据流（Room 写入后自动通知观察者）
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    /**
     * 原子性刷新：先清空再写入，在同一事务内完成
     * 避免 deleteAll + insertUsers 两步之间发生异常导致数据库为空
     */
    @Transaction
    suspend fun replaceAll(users: List<UserEntity>) {
        deleteAll()
        insertUsers(users)
    }
}
