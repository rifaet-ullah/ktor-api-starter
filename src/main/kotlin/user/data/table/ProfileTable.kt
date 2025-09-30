package com.example.user.data.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date

object ProfileTable : LongIdTable("Profile") {
    val firstName = varchar("firstName", length = 50)
    val lastName = varchar("lastName", length = 50)
    val dateOfBirth = date("dateOfBirth")
    val address = optReference("address", AddressTable, onDelete = ReferenceOption.SET_NULL)
    val pictureUri = varchar("pictureUri", length = 256).nullable().default(null)
}
