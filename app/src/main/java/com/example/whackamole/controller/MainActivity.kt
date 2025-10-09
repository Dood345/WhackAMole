package com.example.whackamole.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.whackamole.R
import com.example.whackamole.repository.real.SharedPrefGameRepository
import com.example.whackamole.viewmodel.MainViewModel

/**
 * Main activity that initializes and manages the Whack-a-Mole game.
 *
 * @author Team
 */
class MainActivity : AppCompatActivity() {

    // Sample Initialization of MainViewModel
    private val mainViewModel: MainViewModel by lazy {
        val prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE)
        val repository = SharedPrefGameRepository(prefs)
        MainViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.start_button)
        val clearScoreButton = findViewById<Button>(R.id.clear_score_button)
        val highScoreTextView = findViewById<TextView>(R.id.high_score)

        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        clearScoreButton.setOnClickListener {
            mainViewModel.clearHighScore()
        }

        mainViewModel.highScore.observe(this, Observer { highScore ->
            highScoreTextView.text = "High Score: $highScore"
        })
    }
}