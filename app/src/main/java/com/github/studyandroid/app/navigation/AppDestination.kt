package com.github.studyandroid.app.navigation

import kotlinx.serialization.Serializable

/** Type-Safe Navigation Routes（Kotlin Serialization） */
@Serializable
object UserListDestination

@Serializable
data class UserDetailDestination(val userId: Int)
