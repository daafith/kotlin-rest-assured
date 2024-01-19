package activity

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import generated.resonpse.ActivityResponse
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class BoredTest {

    private val activityTypes =
        listOf("education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork")

    @Test
    fun `should suggest something to do when I feel bored`() {
        val response = getActivity()

        assertAll(
            Executable(assertThat(response.activity)::isNotEmpty),
            Executable(assertThat(response.type in activityTypes)::isTrue)
        )
    }

    @Test
    fun `should suggest something new to do at second time of asking`() {
        val firstTime = getActivity()
        val secondTime = getActivity()

        assertThat(firstTime.activity).isNotEqualTo(secondTime.activity)
    }

    @Test
    fun `should return a specific activity by key`() {
        val keyToFind = "5881028"
        val params = mapOf("key" to keyToFind)

        val response = getActivity(params)

        assertAll(
            Executable { assertThat(response.activity).isEqualTo("Learn a new programming language") },
            Executable { assertThat(response.type).isEqualTo("education") },
            Executable { assertThat(response.participants).isEqualTo(1) },
            Executable { assertThat(response.price).isEqualTo(0.1) },
            Executable { assertThat(response.key).isEqualTo(keyToFind) },
        )
    }

    private fun getActivity(params: Map<String, Any> = mapOf()): ActivityResponse {
        return Given {
            baseUri("http://www.boredapi.com/api/activity/")
            queryParams(params)
        } When {
            get()
        } Then {
            statusCode(200)
        } Extract {
            `as`(ActivityResponse::class.java)
        }
    }
}