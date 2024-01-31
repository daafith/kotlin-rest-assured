package misc

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import org.junit.jupiter.api.Test


class SomeKotestTest {

    @Test
    fun `some nice infixes to make things readable`() {
        "Test" should startWith("T")
        1 + 41 shouldBe 42
    }




}