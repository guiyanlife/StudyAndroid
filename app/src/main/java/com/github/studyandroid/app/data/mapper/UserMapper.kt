package com.github.studyandroid.app.data.mapper

import com.github.studyandroid.app.data.local.entity.UserEntity
import com.github.studyandroid.app.data.remote.dto.UserDto
import com.github.studyandroid.app.domain.model.User

/** DTO → Entity（存入数据库） */
fun UserDto.toEntity(): UserEntity = UserEntity(
    id            = id,
    name          = name,
    username      = username,
    email         = email,
    phone         = phone,
    website       = website,
    companyName   = company.name,
    companySlogan = company.slogan,
    companyBs     = company.bs,
    avatarUrl     = avatarUrl
)

/** Entity → Domain Model（供 UI 消费） */
fun UserEntity.toDomain(): User = User(
    id            = id,
    name          = name,
    username      = username,
    email         = email,
    phone         = phone,
    website       = website,
    company       = companyName,
    companySlogan = companySlogan,
    companyBs     = companyBs,
    avatarUrl     = avatarUrl
)
