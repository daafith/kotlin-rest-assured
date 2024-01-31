package score

class ScoreEvaluation {

    fun grade(score: Int) : String {
        val g = when (score) {
            in 1..55 -> "You failed the test with a score: $score"
            in 56..100 -> "You passed the test with a score: $score"
            else -> throw IllegalArgumentException("Score '$score' is not valid")
        }
        return g
    }
}