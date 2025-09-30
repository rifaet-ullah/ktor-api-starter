package com.example.user.data.entity

import com.example.user.data.table.AddressTable
import com.example.user.domain.models.Address
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AddressEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<AddressEntity>(AddressTable)

    var addressLine by AddressTable.addressLine
    var city by AddressTable.city
    var district by AddressTable.district
    var division by AddressTable.division
    var postalCode by AddressTable.postalCode
    var country by AddressTable.country
}

fun AddressEntity.toAddress() = Address(
    id = id.value,
    addressLine = addressLine,
    city = city,
    district = district,
    division = division,
    postalCode = postalCode,
    country = country,
)
