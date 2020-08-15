package com.mathongo.emojianimate.emojiutils

import java.util.*

class RandomUtil {

    companion object{
        fun generateRandomBetween(start: Int, end: Int): Int {
            val random = Random()
            var rand: Int = random.nextInt(Int.MAX_VALUE - 1) % end
            if (rand < start) {
                rand = start
            }
            return rand
        }
    }
}