package com.example.user.data.entity

import com.example.user.data.table.ProfileTable
import com.example.user.domain.models.Profile
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProfileEntity(id: EntityID<Long>): LongEntity(id) {
    companion object: LongEntityClass<ProfileEntity>(ProfileTable)

    var firstName by ProfileTable.firstName
    var lastName by ProfileTable.lastName
    var dateOfBirth by ProfileTable.dateOfBirth
    var address by AddressEntity optionalReferencedOn ProfileTable.address
    var pictureUri by ProfileTable.pictureUri
}

fun ProfileEntity.toProfile() = Profile(
    id = id.value,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    address = address?.toAddress(),
    pictureUri = pictureUri,
)
