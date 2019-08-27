package com.example.tdm_project.view.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tdm_project.R
import com.example.tdm_project.sharedPreferences.PreferencesProvider
import java.io.File

open class CustomBaseActivity : AppCompatActivity() {

    private lateinit var currentTheme: String
    private lateinit var sharedPref: PreferencesProvider

    companion object {
        var isOnline = true
        var cache : File? = null
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PreferencesProvider(context = this)

        currentTheme = sharedPref.load()

        setAppTheme(currentTheme)

        cache = this.cacheDir
    }

    override fun onResume() {
        super.onResume()
        val theme = sharedPref.load()
        if(currentTheme != theme)
            recreate()
    }

    private fun setAppTheme(currentTheme: String) {
        when (currentTheme) {
            "light" -> setTheme(R.style.AppTheme)
            "dark" -> setTheme(R.style.DarkAppTheme)
        }
    }


}