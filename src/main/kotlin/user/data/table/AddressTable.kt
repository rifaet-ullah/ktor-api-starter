package com.example.user.data.table

import org.jetbrains.exposed.dao.id.LongIdTable

object AddressTable: LongIdTable("address") {
    val addressLine = varchar("addressLine", length = 128)
    val city = varchar("city", length = 128)
    val district = varchar("district", length = 64)
    val division = varchar("division", length = 64)
    val postalCode = integer("postalCode")
    val country = varchar("country", length = 128)
}
