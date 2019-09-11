package com.example.tdm_project.services

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import com.example.tdm_project.services.roomDB.LocalDataBase

class App : Application() {



        companion object {

            const val NOTIFICATION_CHANNEL_ID = "com.example.tdm_project.services.channel"
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

            fun hasNetwork(): Boolean? {
                var isConnected: Boolean? = false // Initial Value
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                if (activeNetwork != null && activeNetwork.isConnected)
                    isConnected = true
                return isConnected
            }

        }

        override fun onCreate() {
            super.onCreate()

            createNotificationChannel()
            context = applicationContext
            db = LocalDataBase.getInstance(context)!!
            isOnline = hasNetwork()!!

        }
        private fun createNotificationChannel(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                var notificationChannel = NotificationChannel( NOTIFICATION_CHANNEL_ID ,
                    "NowDZ NEWS" ,
                    NotificationManager.IMPORTANCE_DEFAULT)

                var notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

    }
