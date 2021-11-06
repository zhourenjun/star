package com.dx.star.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dx.star.appModule
import com.dx.star.util.CrashUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import kotlin.properties.Delegates


/**
 *
 * java类作用描述
 * zrj 2021/8/11 10:08
 * 更新者 2021/8/11 10:08
 */
class App : Application() {

    companion object {
        var context: Context by Delegates.notNull()
            private set

    }

    override fun onCreate() {
        CrashUtil.initCrashLog(this)
        super.onCreate()
        MultiDex.install(this)
        context = this
        startKoin {
            AndroidLogger(Level.DEBUG)
            androidContext(context)
            modules(appModule)
        }
//        Pluto.initialize(this)
//        Pluto.setANRListener(object : ANRListener {
//            override fun onAppNotResponding(exception: ANRException) {
//                exception.printStackTrace()
//                PlutoLog.e("ANR", exception.threadStateMap)
//            }
//        })
    }
}