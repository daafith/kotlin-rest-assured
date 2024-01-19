package activity

import assertk.assertThat
import assertk.assertions.*
import generated.response.ActivityResponse
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource


class BoredTest {

    private val activityTypes =
        listOf("education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork")

    //https://apipheny.io/free-api/
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

    @ParameterizedTest
    @CsvFileSource(resources = ["/csv/activities.csv"], numLinesToSkip = 1)
    fun `should return a specific activity by key`(keyToFind: String, activityName: String, type: String, participants: Int, price: Double) {
        val response = getActivity(mapOf("key" to keyToFind))

        assertAll(
            Executable { assertThat(response.activity).isEqualTo(activityName) },
            Executable { assertThat(response.type).isEqualTo(type) },
            Executable { assertThat(response.participants).isEqualTo(participants) },
            Executable { assertThat(response.price).isEqualTo(price) },
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