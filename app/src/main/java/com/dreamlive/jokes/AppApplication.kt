package com.dreamlive.jokes

import android.app.Application
import android.content.Context
/*import android.support.multidex.MultiDex*/

/**
 * @author 2017/6/21 16:19 / mengwei
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }
}