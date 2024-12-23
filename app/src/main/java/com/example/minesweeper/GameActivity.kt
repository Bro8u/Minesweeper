package com.example.minesweeper

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private var timeInSeconds = 0
    private lateinit var timerTextView: TextView
    private val handler = android.os.Handler()
    private lateinit var runnable: Runnable
    private val rows = 2
    private val columns = 2
    private val victoryCheckHandler = android.os.Handler()
    private val victoryCheckRunnable = object : Runnable {
        override fun run() {
            if (check()) {
                victoryCheckHandler.removeCallbacks(this)
            } else {
                victoryCheckHandler.postDelayed(this, 500)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val restartButton: Button = findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            restartGame()
        }

        timerTextView = findViewById(R.id.timerTextView)
        startTimer()

        val gameGridLayout: GridLayout = findViewById(R.id.gameGridLayout)


        // Добавляем клетки
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val mine = (1..6).random() == 6
                val cell = CellView(this, gameGridLayout, mine, i, j).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 100
                        height = 100
                        marginEnd = 2
                        bottomMargin = 2
                    }
                    setOnClickListener { reveal() }
                    setOnLongClickListener {
                        toggleFlag()
                        true
                    }
                }
                gameGridLayout.addView(cell)
            }
        }
        victoryCheckHandler.post(victoryCheckRunnable)
    }
    fun endGame() {
        handler.removeCallbacks(runnable)
        val text = "Игра окончена! Вы наступили на мину!"
        val size = Toast.LENGTH_LONG
        val toast = Toast.makeText(this, text, size)
        toast.show()
        val gameGridLayout: GridLayout = findViewById(R.id.gameGridLayout)
        // Заблокировать все клетки
        val childCount = gameGridLayout.childCount
        for (i in 0 until childCount) {
            val cell = gameGridLayout.getChildAt(i) as? CellView
            cell!!.setIsEnabled(false) // Отключить нажатия на клетки
        }

        // Показать все мины
        for (i in 0 until childCount) {
            val cell = gameGridLayout.getChildAt(i) as? CellView
            if (cell!!.isMine) {
                cell.reveal()
            }
        }
    }
    private fun startTimer() {
        runnable = Runnable {
            timeInSeconds++
            val minutes = timeInSeconds / 60
            val seconds = timeInSeconds % 60
            timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            handler.postDelayed(runnable, 1000)
        }
        handler.post(runnable)
    }
    private fun restartGame() {
        recreate()
    }
    private fun check(): Boolean{
        val gameGridLayout: GridLayout = findViewById(R.id.gameGridLayout)
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                val linearIndex = i * gameGridLayout.columnCount + j
                val cell = gameGridLayout.getChildAt(linearIndex) as? CellView
                if (cell!!.GetIsMine() && !cell!!.isFlagged) {
                    return false
                }
                if (!cell!!.isRevealed && !cell!!.GetIsMine()) {
                    return false
                }
            }
        }
        handler.removeCallbacks(runnable)
        val text = "Вы ПОБЕДИЛИ!"
        val size = Toast.LENGTH_LONG
        val toast = Toast.makeText(this, text, size)
        toast.show()
        android.os.Handler().postDelayed({
            val intent = android.content.Intent(this@GameActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)

        return true
    }

}