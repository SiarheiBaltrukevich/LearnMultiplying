package com.boltic28.learnmultiplying.models

class Level (
    private val levelNumber: Int,
    private val difficulty: Difficulty,
    val target: Int,
) {

    private var startedAt: Long = 0L
    private var finishedAt: Long = 0L

    private var _progress = 0
    val progress: Int
        get() = _progress

    var isCompleted: Boolean = false
    val name = "Level: $levelNumber"
    val levelTime: Long
        get() = finishedAt - startedAt

    fun start() {
        startedAt = System.currentTimeMillis()
    }

    fun nextSample() : Sample {
        val first = difficulty.firstMultiplierRange.random()
        val second = difficulty.secondMultiplierRange.random()
        val answer = first * second

        return Sample(first, second, answer)
    }

    fun increaseProgress(): Int {
        isCompleted = (++_progress == target)
        return levelNumber
    }

    fun decreaseProgress(): Int {
        if (progress > 0) _progress--
        return -levelNumber
    }

    fun restart() {
        _progress = 0
        startedAt = System.currentTimeMillis()
    }

    fun finish() {
        finishedAt = System.currentTimeMillis() - startedAt
    }
}