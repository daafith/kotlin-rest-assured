package misc

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import score.ScoreEvaluation


class ScoreTest : StringSpec({
    "evaluates to failed" {
        checkAll(Arb.int(1..55)) { a ->
            ScoreEvaluation().grade(a) shouldBe "You failed the test with a score: $a"
        }
    }
    "evaluates to passed" {
        checkAll(Arb.int(56..100)) { a ->
            ScoreEvaluation().grade(a) shouldBe "You passed the test with a score: $a"
        }
    }
    "throws" {
        checkAll(Arb.int(-1..0)) { a ->
            val tooLow = shouldThrow<IllegalArgumentException> {
                ScoreEvaluation().grade(a)
            }
            tooLow.message shouldBe "Score '$a' is not valid"
        }
        checkAll(Arb.int(101..102)) { a ->
            val tooHigh = shouldThrow<IllegalArgumentException> {
                ScoreEvaluation().grade(a)
            }
            tooHigh.message shouldBe "Score '$a' is not valid"
        }
    }
})