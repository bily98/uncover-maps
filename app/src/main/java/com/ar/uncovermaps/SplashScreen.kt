package com.ar.uncovermaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.Thread

class SplashScreen : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        MiHilo()
        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, ArPlayerActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    fun MiHilo(){
        //val thread = Thread(Runnable {
        //    Thread.sleep(600)
        //    led.setImageResource(R.drawable.led_on)

        //})
        var cont = 1
        val timer = object: CountDownTimer(3000, 600) {
            override fun onTick(millisUntilFinished: Long) {

                when (cont) {
                    1 -> led.setImageResource(R.drawable.led_on)
                    2 -> led1.setImageResource(R.drawable.led_on)
                    3 -> led2.setImageResource(R.drawable.led_on)
                    4 -> led3.setImageResource(R.drawable.led_on)
                    5 -> led4.setImageResource(R.drawable.led_on)
                    else -> { // Note the block
                        print("x is neither 1 nor 2")
                    }
                }
                cont++
            }

            override fun onFinish() {cont = 1}
        }
        timer.start()
    }

}