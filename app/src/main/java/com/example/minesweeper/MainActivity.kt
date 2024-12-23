package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настройка кнопки
        val startButton: Button = findViewById(R.id.startButton)
        startButton.setOnClickListener {
            // Переход на экран игры
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}