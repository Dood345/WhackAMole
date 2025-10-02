package com.example.whackamole.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}