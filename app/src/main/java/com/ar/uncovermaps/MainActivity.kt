package com.ar.uncovermaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val CACHED_MODEL_FILE_NAME = "cachedAssetBundle"
        const val KEY_LAST_SAVED_URL = "KEY_LAST_SAVED_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
        initViews()
    }

    private fun setViews() {
        if (Utils.isFileExist(this, CACHED_MODEL_FILE_NAME)) {
            if (getString(R.string.main_default_target_image_url).startsWith("http")) {
                GoView()
            }
        }
    }

    private fun initViews() {
            val url = getString(R.string.main_default_target_modal_url)
            url.isNotBlank().let {
                val lastUrl = Utils.getLastSavedModelUrl(this, KEY_LAST_SAVED_URL)
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
                            CACHED_MODEL_FILE_NAME,
                            url,
                            object : AsyncImageDownloader.ResultListener {
                                override fun actionCompleted(isSuccess: Boolean) {
                                    runOnUiThread {
                                        val errorTextResId =
                                            if (isSuccess) R.string.main_load_asset_bundle_success else R.string.main_load_asset_bundle_fail
                                        Toast.makeText(
                                            this@MainActivity,
                                            errorTextResId,
                                            Toast.LENGTH_LONG
                                        ).show()

                                        GoView()
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

    public fun GoView() {
        val arIntent = Intent(this, ArPlayerActivity::class.java)
        arIntent.putExtra(ArPlayerActivity.KEY_IMAGE_URL, getString(R.string.main_default_target_image_url))
        arIntent.putExtra(ArPlayerActivity.KEY_MODEL_PATH, "$filesDir/$CACHED_MODEL_FILE_NAME")
        startActivity(arIntent)
    }
}