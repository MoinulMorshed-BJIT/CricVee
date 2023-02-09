package com.moinul.cricvee

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.moinul.cricvee.utils.ConnectivityReceiver

class MyApplication : Application() {

    companion object {
        var instance: MyApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}
