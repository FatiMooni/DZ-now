package com.example.tdm_project.services

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.tdm_project.services.roomDB.LocalDataBase

class App : Application() {



        companion object {
            @SuppressLint("StaticFieldLeak")
            @JvmStatic
            lateinit var context: Context
                private set

            @JvmStatic
            lateinit var db: LocalDataBase
                private set

            @JvmStatic
             var isOnline : Boolean = false
                 private set
        }

        override fun onCreate() {
            super.onCreate()

            context = applicationContext
            db = LocalDataBase.getInstance(context)!!
            isOnline = hasNetwork()!!

        }

    private fun hasNetwork(): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
    }
