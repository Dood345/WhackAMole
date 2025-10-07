package com.example.whackamole.controller

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.whackamole.R
import com.example.whackamole.model.GameConfig
import com.example.whackamole.model.Mole
import com.example.whackamole.model.MoleColor
import com.example.whackamole.repository.real.SharedPrefGameRepository
import com.example.whackamole.util.real.AndroidScheduler
import com.example.whackamole.viewmodel.GameViewModel

/**
 * GameActivity controller that manages the Whack-a-Mole game UI.
 * Observes the GameViewModel and updates the view accordingly.
 *
 * @author Controller Team
 */
class GameActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var livesTextView: TextView

    /**
     * List of ImageViews representing mole hills (always visible).
     * These should have IDs like: mole_hill_0, mole_hill_1, ..., mole_hill_8
     */
    private val moleHillViews = mutableListOf<ImageView>()

    /**
     * List of ImageViews representing moles (visibility changes).
     * These should have IDs like: mole_0, mole_1, ..., mole_8
     * They could be layered on top of the hill views (try FrameLayout or RelativeLayout)
     */
    private val moleImageViews = mutableListOf<ImageView>()

    private val viewModel: GameViewModel by lazy {
        val prefs = getSharedPreferences("WhackAMolePrefs", MODE_PRIVATE)
        val repository = SharedPrefGameRepository(prefs)
        val scheduler = AndroidScheduler(mainLooper)
        GameViewModel(repository, scheduler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize UI components
        scoreTextView = findViewById(R.id.score)
        //livesTextView = findViewById(R.id.lives) //TODO: Uncomment when added to XML


        // Set up the mole views and click listeners
        setupMoleViews()

        // Observe score changes
        viewModel.score.observe(this, Observer { score ->
            scoreTextView.text = getString(R.string.score_format, score)
        })

        // Observe misses/lives changes
        viewModel.misses.observe(this, Observer { misses ->
            val livesRemaining = GameConfig.DEFAULT.maxMisses - misses
            livesTextView.text = getString(R.string.lives_format, livesRemaining)
        })

        // Observe mole container changes - this updates ALL 9 positions
        viewModel.moles.observe(this, Observer { moleContainer ->
            // Loop through all 9 moles and update their visibility/color
            moleContainer.moles.forEach { mole ->
                updateMoleView(mole)
            }
        })

        // Observe game over state
        viewModel.gameOver.observe(this, Observer { isGameOver ->
            if (isGameOver) {
                endGame()
            }
        })
    }

    /**
     * Initializes the list of mole ImageViews and sets their click listeners.
     * Assumes your XML has ImageViews with IDs: mole_0, mole_1, ..., mole_8
     * and optionally mole_hill_0, mole_hill_1, ..., mole_hill_8 for backgrounds.
     */
    private fun setupMoleViews() {
        // Hardcoded list of mole IDs - more efficient than getIdentifier()?
        // TODO: Rand create ImageViews with these exact IDs in the XML then we uncomment
//        val moleIds = listOf(
//            R.id.mole_0, R.id.mole_1, R.id.mole_2,
//            R.id.mole_3, R.id.mole_4, R.id.mole_5,
//            R.id.mole_6, R.id.mole_7, R.id.mole_8
//        )
//
//        // Initialize mole ImageViews (the ones that appear/disappear) with kotlin lambda syntax
//        moleIds.forEachIndexed { index, moleId ->
//            val moleView = findViewById<ImageView>(moleId)
//
//            // Set click listener - when clicked, notify ViewModel
//            moleView.setOnClickListener {
//                onMoleWhacked(index)
//            }
//
//            // Initially hide all moles
//            moleView.visibility = View.INVISIBLE
//
//            moleImageViews.add(moleView)
//        }

        // Optional: we can initialize hill ImageViews (always visible backgrounds)
        // we only do this if our XML has separate hill views
//        val hillIds = listOf(
//            R.id.mole_hill_0, R.id.mole_hill_1, R.id.mole_hill_2,
//            R.id.mole_hill_3, R.id.mole_hill_4, R.id.mole_hill_5,
//            R.id.mole_hill_6, R.id.mole_hill_7, R.id.mole_hill_8
//        )
//
//        hillIds.forEach { hillId ->
//            try {
//                val hillView = findViewById<ImageView>(hillId)
//                hillView.visibility = View.VISIBLE  // Always visible
//                moleHillViews.add(hillView)
//            } catch (e: Exception) {
//                // Hill view not found - that's okay, not required
//            }
//        }
    }

    /**
     * Called when a mole ImageView is clicked.
     * Delegates to the ViewModel to handle the hit logic.
     *
     * @param moleId The ID of the mole that was clicked (0-8)
     */
    private fun onMoleWhacked(moleId: Int) {
        viewModel.hitMole(moleId)
    }

    /**
     * Updates a single mole's ImageView based on its state (visible and color).
     * This is called for ALL 9 moles every time the MoleContainer changes.
     *
     * @param mole The mole object containing id, visibility, and color
     */
    private fun updateMoleView(mole: Mole) {
        if (moleImageViews.isEmpty()) {
            return  // Views not initialized yet
        }

        val moleView = moleImageViews[mole.id]

        if (mole.isVisible) {
            // Show the mole
            moleView.visibility = View.VISIBLE

            // Set the background tint color based on mole color
            val colorResId = when (mole.color) {
                MoleColor.RED -> R.color.mole_red
                MoleColor.BLUE -> R.color.mole_blue
                MoleColor.GREEN -> R.color.mole_green
                MoleColor.YELLOW -> R.color.mole_yellow
                MoleColor.PURPLE -> R.color.mole_purple
            }

            // Apply the color tint to the mole ImageView
            moleView.backgroundTintList = ContextCompat.getColorStateList(this, colorResId)

        } else {
            // Hide the mole (hill remains visible if you have separate hill views)
            moleView.visibility = View.INVISIBLE
        }
    }

    /**
     * Called when the game is over. Shows final score and options to restart.
     */
    private fun endGame() {
        val finalScore = viewModel.score.value ?: 0
        val highScore = viewModel.highScore.value ?: 0

        val message = if (finalScore >= highScore) {
            "New High Score: $finalScore!"
        } else {
            "Game Over! Score: $finalScore\nHigh Score: $highScore"
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        // Show AlertDialog with restart and exit options with kotlin lambda syntax taking place of @override
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage(message)
            .setCancelable(false)  // Prevent dismissing by clicking outside
            .setPositiveButton("Restart") { dialog, _ ->
                dialog.dismiss()
                restartGame()
            }
            .setNegativeButton("Main Menu") { dialog, _ ->
                dialog.dismiss()
                finish()  // Closes this activity and returns to MainActivity
            }
            .show()
    }

    /**
     * Restarts the game by calling resetGame() on the ViewModel.
     * Connect this to a "Restart" button in your UI.
     */
    fun restartGame() {
        viewModel.resetGame()
    }
}