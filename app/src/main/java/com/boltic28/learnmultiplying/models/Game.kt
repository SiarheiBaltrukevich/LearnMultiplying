package com.boltic28.learnmultiplying.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Game : ViewModel() {

    private var levelCounter = 0
    private val pointRequires = 15
    private var userScores = 0
    private var comboMultiplicator = 1
    private val maxMultiplicator = 15

    private val levels by lazy {
        listOf(
            Level(++levelCounter, Difficulty(1..3, 1..5), pointRequires),
            Level(++levelCounter, Difficulty(1..3, 6..10), pointRequires),
            Level(++levelCounter, Difficulty(3..5, 3..5), pointRequires),
            Level(++levelCounter, Difficulty(3..5, 6..10), pointRequires),
            Level(++levelCounter, Difficulty(5..8, 6..8), pointRequires),
            Level(++levelCounter, Difficulty(5..8, 7..10), pointRequires),
            Level(++levelCounter, Difficulty(7..10, 4..6), pointRequires),
            Level(++levelCounter, Difficulty(7..10, 7..10), pointRequires),
            Level(++levelCounter, Difficulty(11..15, 1..5), pointRequires),
            Level(++levelCounter, Difficulty(11..15, 6..10), pointRequires),
            Level(++levelCounter, Difficulty(11..15, 11..15), pointRequires),
            Level(++levelCounter, Difficulty(16..20, 1..5), pointRequires),
            Level(++levelCounter, Difficulty(16..20, 6..10), pointRequires),
            Level(++levelCounter, Difficulty(16..20, 11..15), pointRequires),
            Level(++levelCounter, Difficulty(16..20, 16..20), pointRequires),
            Level(++levelCounter, Difficulty(21..50, 1..10), pointRequires),
            Level(++levelCounter, Difficulty(21..50, 11..20), pointRequires),
            Level(++levelCounter, Difficulty(21..50, 21..40), pointRequires),
            Level(++levelCounter, Difficulty(31..50, 31..50), pointRequires),
        )
    }

    private val completedLevels = mutableListOf<Level>()

    private val _levelName = MutableLiveData<String>()
    val levelName: LiveData<String> get() = _levelName

    private val _progress = MutableLiveData<String>()
    val progress: LiveData<String> get() = _progress

    private val _multiplier1 = MutableLiveData<String>()
    val multiplier1: LiveData<String> get() = _multiplier1

    private val _multiplier2 = MutableLiveData<String>()
    val multiplier2: LiveData<String> get() = _multiplier2

    private val _result = MutableLiveData("")
    val result: LiveData<String> get() = _result

    private val _scores = MutableLiveData("0")
    val scores: LiveData<String> get() = _scores

    private var level: Level = levels.first()
    private var sample: Sample = Sample(1, 1, 1)
    private var numberOfLevel: Int = 0

    fun start() {
        nextLevel()
        nextSample()
    }

    fun onButtonClicked(number: Int) {
        val res = _result.value + number
        _result.postValue(res)
    }

    fun onApplyButtonClicked() {
        try {
            val res = Integer.parseInt(_result.value ?: "0")
            checkResult(res)
            nextSample()
            println("->> progress: ${level.progress}")
        } catch (e: NumberFormatException) {
            println("->> error")
        }
    }

    fun onRemoveButtonClicked() {
        val res = _result.value ?: ""
        _result.postValue(res.dropLast(1))
    }

    private fun checkResult(result: Int) {
        val scoresForSample = if (result == sample.answer) {
            println("->> right")
            level.increaseProgress()
        } else {
            println("->> wrong")
            level.decreaseProgress()
        }

        calculateScores(scoresForSample)
        displayProgress()
        displayScores()
    }

    private fun calculateScores(scoresForLevel: Int) {
        if (scoresForLevel < 0) {
            userScores += scoresForLevel
            comboMultiplicator = 1
        } else {
            userScores += (scoresForLevel * comboMultiplicator)
            if (comboMultiplicator < maxMultiplicator) comboMultiplicator++
        }
    }

    private fun displayProgress() {
        val progress = level.progress * 100 / level.target
        _progress.postValue("$progress%")
    }

    private fun displayScores() {
        _scores.postValue("Scores: $userScores")
    }

    private fun nextSample() {
        _result.postValue("")
        if (level.isCompleted) {
            completeLevel()
            nextLevel()
            displayProgress()
            nextSample()
        } else {
            level.nextSample().apply {
                sample = this
                _multiplier1.postValue(multiplier1.toString())
                _multiplier2.postValue(multiplier2.toString())
            }
        }
    }

    private fun nextLevel() {
        level = levels[numberOfLevel++]
        level.start()
        _levelName.postValue(level.name)
    }

    private fun completeLevel() {
        println("->> level is cleared")
        println("->> next level")
        level.finish()
        completedLevels.add(level)
    }


}