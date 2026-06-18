package com.example.jan_aushadhifinder.util

import kotlin.math.min

object FuzzySearch {
    /**
     * Calculates the Levenshtein distance between two strings.
     * Lower distance means higher similarity.
     */
    fun levenshteinDistance(s1: String, s2: String): Int {
        val str1 = s1.lowercase()
        val str2 = s2.lowercase()
        
        val dp = Array(str1.length + 1) { IntArray(str2.length + 1) }

        for (i in 0..str1.length) {
            for (j in 0..str2.length) {
                when {
                    i == 0 -> dp[i][j] = j
                    j == 0 -> dp[i][j] = i
                    else -> {
                        val cost = if (str1[i - 1] == str2[j - 1]) 0 else 1
                        dp[i][j] = min(
                            min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                        )
                    }
                }
            }
        }
        return dp[str1.length][str2.length]
    }

    /**
     * Returns a similarity score between 0.0 and 1.0.
     * 1.0 means exact match.
     */
    fun similarityScore(s1: String, s2: String): Double {
        val distance = levenshteinDistance(s1, s2)
        val maxLength = maxOf(s1.length, s2.length)
        if (maxLength == 0) return 1.0
        return 1.0 - (distance.toDouble() / maxLength)
    }
}
