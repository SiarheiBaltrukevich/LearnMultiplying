package com.boltic28.learnmultiplying

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.boltic28.learnmultiplying.databinding.ActivityMainBinding
import com.boltic28.learnmultiplying.models.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val game: Game = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(Game::class.java)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.game = game
        binding.lifecycleOwner = this

        askForPermission()
        checkAuth()
        game.start()
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 333)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 333) println("->> $grantResults")
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful) {
                println("->> token: ${it.result}")
            } else {
                println("->> error: ${it.exception}")
            }
        }
    }

    private fun checkAuth() {
        if (FirebaseAuth.getInstance().currentUser == null ) logIn() else getToken()
    }

    private fun logIn() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword("t.user@gmail.com", "123456")
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    println("->> user has been logged")
                    getToken()
                } else {
                    createUser()
                    println("->> user logging is failed")
                }
        }
    }

    private fun createUser() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword("t.user@gmail.com", "123456")
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    println("->> user has been registered")
                    logIn()
                    getToken()
                } else {
                    println("->> user registration is failed")
                }

            }
    }
}