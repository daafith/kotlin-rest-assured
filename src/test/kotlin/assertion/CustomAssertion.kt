package assertion

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show

fun Assert<Any>.isInCollection(expected: Collection<Any>) = given { actual ->
    if (actual in expected) return
    expected("to be any of: ${show(expected)} but was: ${show(actual)}")
}