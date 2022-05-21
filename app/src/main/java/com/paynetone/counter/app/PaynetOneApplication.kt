package com.paynetone.counter.app

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.paynetone.counter.main.SplashScreenActivity
import com.paynetone.counter.utils.SharedPref
import kotlinx.coroutines.*


class PaynetOneApplication : MultiDexApplication(), LifecycleObserver {

    var applicationScope :Job?=null
    var mTimeRunning :Boolean = false
    private val timeBackground = 180 // time second
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    companion object {
        var instance: PaynetOneApplication? = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        // App goes to foreground
        if (applicationScope?.isActive==true){
            applicationScope?.cancel()
        }
        if (mTimeRunning){
            SharedPref(this).clear()
            mTimeRunning=false

            val intent = Intent(this, SplashScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
     fun onBackground() {
        // App goes to background
        applicationScope =  CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            for (x in 0..timeBackground step 1) {
                Log.e("TAG", "onBackground: $x" )
                delay(1000)
                if (x==timeBackground) mTimeRunning = true
            }
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope?.cancel()
    }
}
