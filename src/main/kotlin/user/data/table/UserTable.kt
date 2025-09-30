package com.example.user.data.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable : LongIdTable("User") {
    val profile = reference("profile", ProfileTable, onDelete = ReferenceOption.CASCADE)
    val email = varchar("email", length = 256)
    val password = varchar("password", length = 64)
    val salt = varchar("salt", length = 32)
    val role = varchar("role", length = 11)
    val isActive = bool("isActive").default(false)
    val dateJoined = datetime("dateJoined").defaultExpression(CurrentDateTime)
    val lastLogin = datetime("lastLogin").nullable().default(null)
}
