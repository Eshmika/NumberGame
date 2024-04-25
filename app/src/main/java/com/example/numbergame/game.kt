package com.example.numbergame

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.os.CountDownTimer
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class game : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var selections1 = 1
    private var selections2 = 1
    private var score = 0
    private var buttonValue1 = 0
    private var buttonValue2 = 0
    private var displayedValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)

        val button1 = listOf<Button>(
            findViewById(R.id.num_10),
            findViewById(R.id.num_20),
            findViewById(R.id.num_30),
            findViewById(R.id.num_40),
            findViewById(R.id.num_60),
        )

        val button2 = listOf<Button>(
            findViewById(R.id.num_50),
            findViewById(R.id.num_70),
            findViewById(R.id.num_80),
            findViewById(R.id.num_90),
            findViewById(R.id.num_100)
        )


        val timerTextView = findViewById<TextView>(R.id.timerTextView)
        val displayNumTextView = findViewById<TextView>(R.id.display_num)
        val scoreview = findViewById<TextView>(R.id.Scorevalue)
        val submitButton = findViewById<Button>(R.id.submit_button)
        val restartButton = findViewById<Button>(R.id.restart_button)
        val backButton = findViewById<Button>(R.id.bck_button)


        //button1
        for (button in button1) {
            button.setOnClickListener {
                buttonValue1 = button.text.toString().toInt()
                displayedValue = displayNumTextView.text.toString().toInt()

                // Decrement selections and update UI accordingly
                selections1--

                button.setBackgroundColor(Color.parseColor("#FF5733"))

                if (selections1 == 0) {
                    for (btn in button1) {
                        btn.isEnabled = false
                    }
                }
            }
        }

        //button2
        for (button in button2) {
            button.setOnClickListener {
                buttonValue2 = button.text.toString().toInt()
                displayedValue = displayNumTextView.text.toString().toInt()

                // Decrement selections and update UI accordingly
                selections2--

                button.setBackgroundColor(Color.parseColor("#FF5733"))

                if (selections2 == 0) {
                    for (btn in button2) {
                        btn.isEnabled = false
                    }
                }
            }
        }

        submitButton.setOnClickListener {

            // Stop the CountDownTimer
            countDownTimer?.cancel()

            if (buttonValue1 == displayedValue && buttonValue2 == displayedValue) {

                //count score
                score++
                scoreview.text = score.toString()

                Toast.makeText(this, "Good..", Toast.LENGTH_SHORT).show()

                buttonValue1 = 0
                buttonValue2 = 0

                restartButton.performClick()


            } else {

                Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show()

                // Retrieve existing "game_result" data
                val existingData = sharedPreferences.getString("game_result", "")

                if (score != 0) {
                    // Create new game result entry with current date and time
                    val currentDateTime =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val newGameResult = "$currentDateTime - Score $score"

                    // Combine new data with existing data, separated by newline
                    val updatedGameResult = if (existingData.isNullOrEmpty()) {
                        newGameResult
                    } else {
                        // Split existing data into lines
                        val lines = existingData.split("\n")
                        // If the number of lines exceeds 10, remove the last line (oldest data)
                        val updatedLines = if (lines.size >= 5) {
                            lines.dropLast(1)
                        } else {
                            lines
                        }
                        // Combine updated lines with new data
                        val updatedData = updatedLines.joinToString("\n")
                        "$newGameResult\n$updatedData"
                    }

                    // Save updated game result data to SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("game_result", updatedGameResult)
                    editor.apply()
                }

                //reset score
                score = 0
                scoreview.text = score.toString()

            }
        }

        backButton.setOnClickListener {
            // Stop the CountDownTimer
            countDownTimer?.cancel()



            var intent = Intent(this,mainmenu::class.java)
            startActivity(intent)
            finish()

        }

        val TotalValuNumbers = arrayOf(150, 20, 110, 130, 70)
        val Button_numbers1 = arrayOf(150, 20, 110, 130, 70)
        val Button_numbers2 = arrayOf(150, 20, 110, 130, 70)

        restartButton.setOnClickListener {
            // Stop the CountDownTimer
            countDownTimer?.cancel()

            selections1 = 1
            selections2 = 1

            // Enable all buttons
            for (button in button1) {
                button.isEnabled = true
                button.setBackgroundColor(Color.parseColor("#4CAF50")) // Reset button color
            }

            for (button in button2) {
                button.isEnabled = true
                button.setBackgroundColor(Color.parseColor("#4CAF50")) // Reset button color
            }

            val randomIndex = (0 until TotalValuNumbers.size).random()

            // Get the randomly selected number
            val randomNumber = TotalValuNumbers[randomIndex]

            // Set the text of displayNumTextView to the randomly selected number
            displayNumTextView.text = randomNumber.toString()

            // Shuffle the Button_numbers array
            Button_numbers1.shuffle()
            Button_numbers2.shuffle()

            // Assign shuffled numbers to buttons
            for ((index, button) in button1.withIndex()) {
                button.text = Button_numbers1[index].toString()
            }

            for ((index, button) in button2.withIndex()) {
                button.text = Button_numbers2[index].toString()
            }

            // Start the countdown timer
            countDownTimer = object : CountDownTimer(11000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Timer is ticking down
                    timerTextView.text = "Time Remaining: ${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    // Timer finished, automatically click submitButton
                    Toast.makeText(this@game,"Time is over", Toast.LENGTH_SHORT).show()
                    submitButton.performClick()

                }
            }.start()
        }

        restartButton.performClick()
    }
}