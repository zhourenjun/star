package com.dx.star.util

import androidx.viewbinding.BuildConfig


/**
 * 常量
 * zrj 2020/7/8
 */
object Constant {
    const val HOST_TEST = "http://lpkf.api.yocalydev.cn:8029"
    const val HOST_ONLINE = "http://rehab.api.yocaly.cn"
    val BASE_URL = if (BuildConfig.DEBUG) HOST_TEST else HOST_ONLINE
    const val APP_KEY = "e4786079aa10472591e9d20bdb0efe78"
    const val APP_SECRET = "c466d6a01bd74cda91e1aabd54bf3759"
    const val DEVICE_UNIQUE_CODE = "device_unique_code"
    const val NEW_BASE_URL = "newBaseUrl"
    val SDCARD_DIR = android.os.Environment.getExternalStorageDirectory().absolutePath

}
