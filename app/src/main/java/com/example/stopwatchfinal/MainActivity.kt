package com.example.stopwatchfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var shareButton: Button

    private var isRunning = false
    private var timeInMillis: Long = 0

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            timeInMillis += 100
            timerText.text = formatTime(timeInMillis)
            handler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText = findViewById(R.id.timerText)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        shareButton = findViewById(R.id.shareButton)

        startButton.setOnClickListener { startTimer() }
        pauseButton.setOnClickListener { pauseTimer() }
        stopButton.setOnClickListener { stopTimer() }
        shareButton.setOnClickListener { shareTime() }
    }

    private fun startTimer() {
        if (!isRunning) {
            handler.post(runnable)
            isRunning = true
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    private fun stopTimer() {
        handler.removeCallbacks(runnable)
        isRunning = false
        timeInMillis = 0
        timerText.text = "00:00:00"
    }

    private fun shareTime() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Stopwatch Time: ${formatTime(timeInMillis)}")
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / 60000
        val seconds = (millis / 1000) % 60
        val centiseconds = (millis / 10) % 100
        return String.format("%02d:%02d:%02d", minutes, seconds, centiseconds)
    }
}
