package com.example.excercise1

import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var num = 0
    private val DELAY_TIME_COUNT = 50L
    private val DELAY_TIME_START = 1000L
    private val DELAY_LONG_PRESS = 500L
    private val DELAY_INCREASE = 10L
    var checkAdd = false
    var checkMinus = false
    var j1: Job? = null
    var arrColor =
        listOf(Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.LTGRAY,
            Color.RED, Color.GREEN, Color.MAGENTA, Color.YELLOW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var job: Job? = null

        val gestureDetectorMinus =
            GestureDetector(applicationContext, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    timeDown()
                    job?.cancel()
                    btnMinus.setBackgroundResource(R.color.green)
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    btnMinus.setBackgroundResource(R.color.green)
                    checkMinus = true
                    j1 = CoroutineScope(Dispatchers.Main).launch {
                        delay(DELAY_LONG_PRESS)
                        while (true) {
                            delay(DELAY_INCREASE)
                            timeDown()
                        }
                    }
                }
            }
            )
        val gestureDetectorAdd =
            GestureDetector(applicationContext, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    btnAdd.setBackgroundResource(R.color.green)
                    timeUp()
                    job?.cancel()
                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    checkAdd = true
                    btnAdd.setBackgroundResource(R.color.green)
                    j1 = CoroutineScope(Dispatchers.Main).launch {
                        delay(DELAY_LONG_PRESS)
                        while (true) {
                            delay(DELAY_INCREASE)
                            timeUp()
                        }
                    }
                }
            }
            )

        btnAdd.setOnTouchListener { view, motionEvent ->
            gestureDetectorAdd.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                btnAdd.setBackgroundResource(R.color.white_blue)
                j1?.cancel()
                if (!checkMinus)
                    job = timeChange()
                checkAdd = false
            }
            true
        }
        btnMinus.setOnTouchListener { view, motionEvent ->
            textChangeColor()
            gestureDetectorMinus.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                btnMinus.setBackgroundResource(R.color.white_blue)
                j1?.cancel()
                if (!checkAdd)
                    job = timeChange()
                checkMinus = false
            }
            true
        }

        var ox: Float = 0f
        var oy: Float = 0f
        var x: Float
        var y: Float
        var _x: Float = 0f
        var _y: Float = 0f
        relativeLayout.setOnTouchListener { view, motionEvent ->
            textChangeColor()
            job?.cancel()
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    x = motionEvent.rawX
                    y = motionEvent.rawY
                    if (y < oy) {
                        if (y > _y)
                            timeDown()
                        else if (y < _y)
                            timeUp()

                    } else if (y > oy) {
                        if (y < _y)
                            timeUp()
                        else if (y > _y)
                            timeDown()
                    }
                    _x = x
                    _y = y
                }
                MotionEvent.ACTION_DOWN -> {
                    ox = motionEvent.rawX
                    oy = motionEvent.rawY
                }
                MotionEvent.ACTION_UP -> job = timeChange()
            }
            true
        }

    }

    private fun timeChange(): Job = lifecycleScope.launch(Dispatchers.Main) {
        delay(DELAY_TIME_START)
        if (num > 0) {
            while (num > 0) {
                num -= 1
                delay(DELAY_TIME_COUNT)
                textView.text = num.toString()
            }
        } else {
            while (num < 0) {
                num += 1
                delay(DELAY_TIME_COUNT)
                textView.text = num.toString()
            }
        }
    }

    private fun timeDown() {
        num--
        textChangeColor()
        textView.text = num.toString()
    }

    private fun timeUp() {
        num++
        textChangeColor()
        textView.text = num.toString()

    }

    private fun textChangeColor() {
        val numb = abs(textView.text.toString().toInt())
        if (numb % 100 == 0 && numb >= 100) {
            var index = arrColor.random()
            textView.setTextColor(index)
        }
    }
}