package com.example.excercise1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var num = 0
    private val DELAY_TIME_COUNT = 100L
    private val DELAY_TIME_START = 1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var job: Job? = null
        btnAdd.setOnClickListener {
            num++
            textView.text = num.toString()
            job?.cancel()
            job = lifecycleScope.launch(Dispatchers.Main + Job()) {
                delay(DELAY_TIME_START)
                if (num > 0)
                    timeDown()
                else
                    timeUp()
            }
        }
        btnMinus.setOnClickListener {
            num--
            textView.text = num.toString()
            job?.cancel()
            job = lifecycleScope.launch(Dispatchers.Main + Job()) {
                delay(DELAY_TIME_START)
                if (num > 0)
                    timeDown()
                else
                    timeUp()
            }
        }
    }

    private suspend fun timeDown() {
        while (num > 0) {
            num -= 1
            delay(DELAY_TIME_COUNT)
            textView.text = num.toString()
        }
    }

    private suspend fun timeUp() {
        while (num < 0) {
            num += 1
            delay(DELAY_TIME_COUNT)
            textView.text = num.toString()
        }
    }
}