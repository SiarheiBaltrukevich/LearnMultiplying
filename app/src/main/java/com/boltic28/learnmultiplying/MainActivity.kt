package com.boltic28.learnmultiplying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.boltic28.learnmultiplying.databinding.ActivityMainBinding
import com.boltic28.learnmultiplying.models.Game

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val game: Game = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(Game::class.java)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.game = game
        binding.lifecycleOwner = this


        game.start()
    }
}