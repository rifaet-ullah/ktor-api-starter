package com.example.user.data.entity

import com.example.user.data.table.UserTable
import com.example.user.domain.models.Password
import com.example.user.domain.models.Role
import com.example.user.domain.models.User
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UserTable)

    var profile by ProfileEntity referencedOn UserTable.profile
    var email by UserTable.email
    var password by UserTable.password
    var salt by UserTable.salt
    var role by UserTable.role
    var isActive by UserTable.isActive
    var dateJoined by UserTable.dateJoined
    var lastLogin by UserTable.lastLogin
}

fun UserEntity.toUser() = User(
    id = id.value,
    profile = profile.toProfile(),
    email = email,
    password = Password(value = password, salt = salt),
    role = Role.valueOf(role),
    isActive = isActive,
    dateJoined = dateJoined,
    lastLogin = lastLogin,
)
