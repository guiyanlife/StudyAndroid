package com.github.studyandroid.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.studyandroid.app.data.local.dao.UserDao
import com.github.studyandroid.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = true   // 生成 JSON schema，支持版本 diff 和 autoMigrations
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
