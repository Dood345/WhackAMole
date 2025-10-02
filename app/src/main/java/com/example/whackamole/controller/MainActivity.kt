package com.example.whackamole.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.whackamole.GameController
import com.example.whackamole.GameModel
import com.example.whackamole.GameView
import com.example.whackamole.R
import com.example.whackamole.repository.real.SharedPrefGameRepository
import com.example.whackamole.viewmodel.MainViewModel

/**
 * Main activity that initializes and manages the Whack-a-Mole game.
 *
 * @author Team
 */
class MainActivity : AppCompatActivity() {

    private lateinit var model: GameModel
    private lateinit var view: GameView
    private lateinit var controller: GameController

    // Sample Initialization of MainViewModel
    private val mainViewModel: MainViewModel by lazy {
        val prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE)
        val repository = SharedPrefGameRepository(prefs)
        MainViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize MVC components
        model = GameModel()
        view = GameView(this)
        controller = GameController(model, view)
    }
}