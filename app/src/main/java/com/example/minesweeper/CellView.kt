package com.example.minesweeper

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CellView(context: Context, private val gameGridLayout: GridLayout, _isMine: Boolean, x: Int, y: Int) : FrameLayout(context) {
    var isRevealed: Boolean = false
    var isMine: Boolean = false
    var isFlagged: Boolean = false
    var neighboringMines: Int = 0
    var isEnabledCell: Boolean = true
    var X = 0
    var Y = 0


    private val textView: TextView

    init {
        // Настройка внешнего вида клетки
        setBackgroundColor(Color.LTGRAY) // Серый фон
        textView = TextView(context).apply {
            gravity = Gravity.CENTER
            textSize = 16f
            setTextColor(Color.BLACK)
            visibility = GONE // Скрываем текст по умолчанию
        }
        addView(textView)
        isMine = _isMine
        X = x
        Y = y
    }

    // Метод для открытия клетки
    fun reveal() {
        if (isRevealed) return
        isRevealed = true
        setBackgroundColor(Color.WHITE)
        if (isMine) {
            textView.text = "💣"
            textView.visibility = VISIBLE
            if (isEnabledCell) {
                (context as GameActivity).endGame()
                android.os.Handler().postDelayed({
                    val intent = android.content.Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as AppCompatActivity).finish() // Закрыть текущую активность
                }, 5000)
            }
        } else {
            for (i in X - 1 .. X + 1) {
                for (j in Y - 1 .. Y + 1) {
                    if (i < 0 || j < 0 || i >= gameGridLayout.rowCount || j >= gameGridLayout.columnCount) continue
                    val linearIndex = i * gameGridLayout.columnCount + j
                    val cell = gameGridLayout.getChildAt(linearIndex) as? CellView
                    if (cell != null && cell.GetIsMine()) {
                        neighboringMines++
                    }
                }
            }
            textView.text = neighboringMines.toString()
            textView.visibility = VISIBLE
        }
    }

    // Метод для установки флага
    fun toggleFlag() {
        if (isRevealed) return
        isFlagged = !isFlagged
        textView.text = if (isFlagged) "🚩" else ""
        textView.visibility = VISIBLE
    }
    fun GetIsMine(): Boolean {
        return isMine
    }
    fun setIsEnabled(value: Boolean) {
        isEnabledCell = value
    }

}