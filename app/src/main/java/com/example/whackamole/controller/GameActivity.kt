package com.example.whackamole.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.whackamole.R
import com.example.whackamole.repository.real.SharedPrefGameRepository
import com.example.whackamole.util.real.AndroidScheduler
import com.example.whackamole.viewmodel.GameViewModel


class GameActivity : AppCompatActivity() {

    // Sample Initialization of GameViewModel
    private val viewModel: GameViewModel by lazy {
        val prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE)
        val repository = SharedPrefGameRepository(prefs)
        val scheduler = AndroidScheduler(mainLooper)
        GameViewModel(repository, scheduler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}