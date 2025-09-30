package com.example.user

import com.example.module
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.server.testing.testApplication
import org.junit.Test

class TestAddUser {
    @Test
    fun `returns the new added user when the information is valid`() = testApplication{
        application { module() }

        client.post("/auth/register") {
            setBody(
                mapOf(
                    "firstName" to "First",
                    "lastName" to "Last"
                )
            )
        }
    }

    @Test
    fun `fails to add the user when the user already exists`(){}

    @Test
    fun `fails to add user when profile cannot be added`(){}
}
