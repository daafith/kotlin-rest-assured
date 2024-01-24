package activity

import assertion.isInCollection
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import generated.response.ActivityResponse
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.EnumSource


class BoredTest {

    private val activityTypes =
        listOf("education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork")

    enum class ActivityType(val activityName: String) {
        EDUCATION("education"),
        RECREATIONAL("recreational"),
        SOCIAL("social"),
        DIY("diy"),
        CHARITY("charity"),
        COOKING("cooking"),
        RELAXATION("relaxation"),
        MUSIC("music"),
        BUSYWORK("busywork")
    }


    //https://apipheny.io/free-api/
    @Test
    fun `should suggest something to do when I feel bored`() {
        val response = performGetActivity()

        assertAll {
            assertThat(response.activity)::isNotEmpty
            assertThat(response.type).isInCollection(activityTypes)
        }
    }

    @Test
    fun `should suggest something new to do at second time of asking`() {
        val firstTime = performGetActivity()
        val secondTime = performGetActivity()

        assertThat(firstTime.activity).isNotEqualTo(secondTime.activity)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/csv/activities.csv"], numLinesToSkip = 1)
    fun `should return a specific activity by key`(
        keyToFind: String,
        activityName: String,
        type: String,
        participants: Int,
        price: Double
    ) {
        val response = performGetActivity(mapOf("key" to keyToFind))

        assertAll {
            assertThat(response.activity).isEqualTo(activityName)
            assertThat(response.type).isEqualTo(type)
            assertThat(response.participants).isEqualTo(participants)
            assertThat(response.price).isEqualTo(price)
            assertThat(response.key).isEqualTo(keyToFind)
        }
    }

    @ParameterizedTest
    @EnumSource(ActivityType::class)
    fun `should find an activity by type`(activityType: ActivityType) {
        val response = performGetActivity(mapOf("type" to activityType.activityName))

        assertThat(response.type).isEqualTo(activityType.activityName)
    }

    private fun performGetActivity(params: Map<String, Any> = mapOf()): ActivityResponse {
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