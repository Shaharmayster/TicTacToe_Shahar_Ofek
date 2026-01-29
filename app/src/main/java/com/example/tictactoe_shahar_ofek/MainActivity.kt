package com.example.tictactoe_shahar_ofek

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvStatus: TextView
    private lateinit var btnPlayAgain: Button
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private val board = Array(3) { Array(3) { "" } }
    private var playerX = true // true for X, false for O
    private var turnCount = 0
    private var gameActive = true
    private var defaultButtonTextColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)

        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "btn$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }
        // Store the default text color to reset it later
        defaultButtonTextColor = buttons[0][0]?.currentTextColor ?: Color.BLACK


        btnPlayAgain.setOnClickListener {
            playAgain()
        }
    }

    override fun onClick(v: View?) {
        if (!gameActive || v !is Button || v.text.toString() != "") {
            return
        }

        val (row, col) = getButtonPosition(v.id)

        if (board[row][col] == "") {
            turnCount++
            val symbol = if (playerX) "X" else "O"
            board[row][col] = symbol
            v.text = symbol
            if (playerX) {
                v.setTextColor(Color.BLUE)
            } else {
                v.setTextColor(Color.RED)
            }

            if (checkForWin()) {
                gameActive = false
                val winner = if (playerX) "X" else "O"
                tvStatus.text = "$winner wins!"
                tvStatus.textSize = 30f
                btnPlayAgain.visibility = View.VISIBLE
                disableBoard()
            } else if (turnCount == 9) {
                gameActive = false
                tvStatus.text = "Draw!"
                tvStatus.textSize = 30f
                btnPlayAgain.visibility = View.VISIBLE
                disableBoard()
            } else {
                playerX = !playerX
                tvStatus.text = "Turn: ${if (playerX) "X" else "O"}"
            }
        }
    }

    private fun checkForWin(): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != "") {
                return true
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] != "") {
                return true
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != "") {
            return true
        }
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != "") {
            return true
        }

        return false
    }

    private fun getButtonPosition(resId: Int): Pair<Int, Int> {
        for (i in 0..2) {
            for (j in 0..2) {
                if (buttons[i][j]?.id == resId) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1) // Should not happen
    }

    private fun playAgain() {
        playerX = true
        turnCount = 0
        gameActive = true
        tvStatus.text = "Turn: X"
        tvStatus.textSize = 24f
        btnPlayAgain.visibility = View.GONE

        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ""
                buttons[i][j]?.text = ""
                buttons[i][j]?.setTextColor(defaultButtonTextColor)
                buttons[i][j]?.isEnabled = true
            }
        }
    }

    private fun disableBoard(){
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.isEnabled = false
            }
        }
    }
}
