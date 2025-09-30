package com.example.user.data.service

import com.example.user.domain.models.Password
import com.example.user.domain.service.PasswordService
import java.security.MessageDigest
import java.security.SecureRandom

class SHA256PasswordService : PasswordService {
    private val secureRandom = SecureRandom()
    private val messageDigest = MessageDigest.getInstance("SHA-256")

    override fun generate(password: String): Password {
        val saltBytes = ByteArray(16)
        secureRandom.nextBytes(saltBytes)
        val salt = saltBytes.fold("") { str, x -> str + "%20".format(x) }
        val hashedValue = messageDigest
            .digest("$password$salt".toByteArray())
            .fold("") { str, x -> str + "%20".format(x) }

        return Password(value = hashedValue, salt = salt)
    }

    override fun verify(password: String, hashedPassword: Password): Boolean {
        val hashedValue = messageDigest
            .digest("$password${hashedPassword.salt}".toByteArray())
            .fold("") { str, x -> str + "%20".format(x) }

        return hashedValue == hashedPassword.value
    }
}
