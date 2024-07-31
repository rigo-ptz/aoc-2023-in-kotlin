/**
 * GIVEN
 * Game where random number of Red, Green, Blue cubes is hidden in bag.
 * Game consists from several rounds.
 * In each round we grab a handful of random cubes from bag and then put them back.
 * ---
 * EXAMPLE INPUT
 * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green 
 * -----
 * FIND
 * 1) Determine which games would have been possible if the bag had been loaded with only
 *      12 red cubes, 13 green cubes, and 14 blue cubes.
 *      What is the sum of the IDs of those games?
 * 
 * 2) In each game find fewest number of cubes of each color that could have been in the bag to make the game possible.
 *    Then multiply them and sum up results of each game.
 * -----
 * CONSTRAINTS
 * -----
 * SOLUTION
 * 1)
 * - Parse line by line, check that amount of cubes of color is less than given corressponding amount
 * - Main difficulty here is in parsing.
 * - We can use regex, what simplify the solution, but can have negative impact on performance due to a several runs (one for red, one for green, one for blue with "(\d{1}) red" pattern)
 * - Another approach would be in iterating over chars and checking Int item and "it's pos + 2" item. In this case we assume that input is always correct
 * -
 * - parseGameAndGetId is incorrect (or unoptimal) as it reads same items multiple times
 */

fun main() {
    val daySolver = Day02()
    
    // Part 1
    //// Test
//    check(daySolver.solvePart1(readInput("Day02_test1")) == 8)
    //// Contest
//    daySolver.solvePart1(readInput("Day02"))
    
    // Part 2
    //// Test
//    check(daySolver.solvePart2(readInput("Day02_test1")) == 2286)
    daySolver.solvePart2(readInput("Day02"))
}

class Day02 : DaySolver() {
    override fun solvePart1(input: List<String>?): Int {
        var sum = 0
        input?.forEach {
            val id = parseGameAndGetId(it)
            if (id != null) sum += id
        }
        println("Sum=$sum")
        return sum
    }

    private fun parseGameAndGetId(game: String): Int? {
        val lineLength = game.length
        
        if (lineLength < 6) return null
            
        val (gameId, semicolonIndex) = readGameId(game)
        
        var correct = true
        for (i in semicolonIndex..<lineLength) {
            if (game[i].isDigit()) {
                val (n, whiteSpaceIndex) = readDigit(game, i)
                
                when (game[whiteSpaceIndex + 1]) {
                    'r' -> {
                        if (n > 12) {
                            correct = false
                            break
                        }
                    }
                    'g' -> {
                        if (n > 13) {
                            correct = false
                            break
                        }
                    }
                    'b' -> {
                        if (n > 14) {
                            correct = false
                            break
                        }
                    }
                }
            }
        }
        
        return if (correct) {
            println("Correct Game: $gameId")
            gameId
        } else {
            null
        }
    }

    private fun readGameId(game: String): Pair<Int, Int> {
        var res = 0
        var index = 5
        while (game[index] != ':') {
            if (game[index].isDigit()) {
                res = res * 10 + game[index].digitToInt()
            } else {
                break
            }
            index += 1
        }
        return res to index
    }

    private fun readDigit(game: String, startIndex: Int): Pair<Int, Int> {
        var res = 0
        var index = startIndex
        while (game[index] != ' ') {
            if (game[index].isDigit()) {
                res = res * 10 + game[index].digitToInt()
            } else {
                break
            }
            index += 1
        }
        return res to index
    }

    override fun solvePart2(input: List<String>?): Int {
        val sum = input?.sumOf {
            parseGame2(it)
        } ?: 0
        println("Sum=$sum")
        return sum
    }

    private fun parseGame2(game: String): Int {
        val lineLength = game.length
        
        if (lineLength < 6) return 0
        
        val startIndex = game.indexOf(':')
        
        var rResult = 0
        var gResult = 0
        var bResult = 0
        
        var digitParsingInProgress = false
        var lastParsedInt: Int? = null
        
        for (i in startIndex..<lineLength) {
            val c = game[i]
            
            when {
                c.isDigit() -> {
                    lastParsedInt = (lastParsedInt ?: 0) * 10 + c.digitToInt()
                }
                
                c == 'r' && game[i-1] == ' ' -> {
                    if (lastParsedInt!!.compareTo(rResult) > 0) {
                        rResult = lastParsedInt
                    }
                    lastParsedInt = null
                }
                
                c == 'g' -> {
                    if (lastParsedInt!!.compareTo(gResult) > 0) {
                        gResult = lastParsedInt
                    }
                    lastParsedInt = null
                }
                
                c == 'b' -> {
                    if (lastParsedInt!!.compareTo(bResult) > 0) {
                        bResult = lastParsedInt
                    }
                    lastParsedInt = null
                }
            }
        }
        
        return  rResult * gResult * bResult
    }

}

data class Game(
    val id: Int,
    val nRed: Int,
    val nGreen: Int,
    val nBlue: Int,
)