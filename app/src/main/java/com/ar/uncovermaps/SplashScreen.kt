package com.ar.uncovermaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.lang.Thread

class SplashScreen : AppCompatActivity() {
    companion object {
        const val CACHED_MODEL_FILE_NAME = "cachedAssetBundle"
        const val KEY_LAST_SAVED_URL = "KEY_LAST_SAVED_URL"
    }

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
//        setViews()
//        initViews()
        MiHilo()
        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
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

    private fun setViews() {
        if (Utils.isFileExist(this, SplashScreen.CACHED_MODEL_FILE_NAME)) {
            val arIntent = Intent(this, ArPlayerActivity::class.java)

            val url: String = getString(R.string.main_default_target_image_url)
            arIntent.putExtra(ArPlayerActivity.KEY_IMAGE_URL, url)
            arIntent.putExtra(ArPlayerActivity.KEY_MODEL_PATH, "$filesDir/${SplashScreen.CACHED_MODEL_FILE_NAME}")
            startActivity(arIntent)
            finish()
        }
    }

    private fun initViews() {
        var url = ""
        val lastUrl = Utils.getLastSavedModelUrl(this, SplashScreen.KEY_LAST_SAVED_URL)
        lastUrl?.let { safeLastUrl ->
            url = safeLastUrl
        }

        url.isNotBlank().let {
            val lastUrl = Utils.getLastSavedModelUrl(this, SplashScreen.KEY_LAST_SAVED_URL)
            when {
                url == lastUrl -> {
                    Toast.makeText(
                        this,
                        R.string.main_load_asset_bundle_already_downloaded,
                        Toast.LENGTH_LONG
                    )
                }
                url.isNotBlank() && url.startsWith("http") -> {
                    //Download Search Image
                    AsyncImageDownloader(
                        this,
                        SplashScreen.CACHED_MODEL_FILE_NAME,
                        url,
                        object : AsyncImageDownloader.ResultListener {
                            override fun actionCompleted(isSuccess: Boolean) {
                                runOnUiThread {
                                    val errorTextResId =
                                        if (isSuccess) R.string.main_load_asset_bundle_success else R.string.main_load_asset_bundle_fail
                                    Toast.makeText(
                                        this@SplashScreen,
                                        errorTextResId,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    ).execute()
                }
                else -> {
                }
            }
        }
    }
}