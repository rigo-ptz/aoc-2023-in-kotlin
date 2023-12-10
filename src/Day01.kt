import kotlin.time.measureTimedValue

fun main() {

    fun part1(input: List<String>): Int {
        val timedResult = measureTimedValue {
            input.sumOf { row ->
                getCallibrationValuePart1(row) //1.284542ms
            }
        }
        println("Result: ${timedResult.value}, Time spent: ${timedResult.duration}")
        return timedResult.value
    }

    fun part2(input: List<String>): Int {
        val timedResult = measureTimedValue {
            val split1 = input.spliterator()
            val split2 = split1.trySplit()
            var sum = 0

            split1.forEachRemaining { row ->
                sum += getCallibrationValuePart2(row)
            }

            split2.forEachRemaining { row ->
                sum += getCallibrationValuePart2(row)
            }

            sum
        }
        println("Result: ${timedResult.value}, Time spent: ${timedResult.duration}")
        return timedResult.value
    }

    val day01Input = readInput("Day01")

    // Part 1
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    check(part1(day01Input) == 54338)

    // Part 2
    val test2Input = readInput("Day01_test2")
    check(part2(test2Input) == 281)
    check(part2(day01Input) == 53389)
}

private val DIGITS = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)

private fun getCallibrationValuePart1(row: String): Int =
    row.first { it.isDigit() }.digitToInt() * 10 + row.last { it.isDigit() }.digitToInt()

private fun getCallibrationValuePart2(row: String): Int {
    val digitsHolder = StringBuilder()
    val needToCheckForSubstrings = row.length >= 3
    val result: Int = if (!needToCheckForSubstrings) {
        getCallibrationValuePart1(row)
    } else {
        row.forEachIndexed { index, char ->
            if (char.isDigit()) {
                digitsHolder.append(char)
            } else {
                DIGITS.forEach { (digitString, digit) ->
                    if (row.startsWith(digitString, index, ignoreCase = true)) {
                        digitsHolder.append(digit)
                    }
                }
            }
        }

        val r = digitsHolder.first().digitToInt() * 10 + digitsHolder.last().digitToInt()
        digitsHolder.clear()
        r
    }
    return result
}
