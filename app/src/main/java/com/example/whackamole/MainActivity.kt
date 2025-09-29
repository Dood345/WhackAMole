package com.example.whackamole

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity that initializes and manages the Whack-a-Mole game.
 *
 * @author Team
 */
class MainActivity : AppCompatActivity() {

    private lateinit var model: GameModel
    private lateinit var view: GameView
    private lateinit var controller: GameController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize MVC components
        model = GameModel()
        view = GameView(this)
        controller = GameController(model, view)
    }
}
