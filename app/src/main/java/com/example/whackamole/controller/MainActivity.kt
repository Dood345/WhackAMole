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
 * @author Jesutofunmi Obimakinde, Rand Roman, Daniel Ripley
 */
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        val prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE)
        val repository = SharedPrefGameRepository(prefs)
        MainViewModel(repository)
    }

    private lateinit var highScoreTextView: TextView;


    /**
     * Initializes the activity, setting up the user interface and view model.
     * This function is called when the activity is first created. It inflates the layout,
     * finds UI elements, and sets up listeners for the start and clear score buttons.
     * It also observes the high score from the view model to keep the display updated.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        highScoreTextView = findViewById<TextView>(R.id.high_score)
        val startButton = findViewById<Button>(R.id.start_button)
        val clearScoreButton = findViewById<Button>(R.id.clear_score_button)

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

    /**
     * Called when returning to main window after finishing a round.
     * At this point, the activity is at the top of the activity stack, with user input going to it.
     * This implementation ensures the high score is fresh every time the user returns to the main screen.
     */
    override fun onResume() {
        super.onResume()
        mainViewModel.highScore.observe(this, Observer { highScore ->
            highScoreTextView.text = "High Score: $highScore"
        })
    }
}