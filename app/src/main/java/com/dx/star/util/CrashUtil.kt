package com.dx.star.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.viewbinding.BuildConfig
import com.dx.star.ext.toJson
import com.dx.star.util.Constant.SDCARD_DIR
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * java类作用描述
 * zrj 2021/8/11 16:51
 * 更新者 2021/8/11 16:51
 */
object CrashUtil {

    @SuppressLint("SimpleDateFormat")
    fun initCrashLog(context: Context) {
        val oriHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            try {
                val buffer = StringBuilder()
                buffer.append(getCurProcessName(context))
                    .append("\n")
                    .append(getPhoneInfo())
                    .append("\n")
                    .append(getCrashInfo(e))
                val log = buffer.toString()
                //发送崩溃日志
                sendExceptionLog(log)
                saveLogLocal("$SDCARD_DIR/crash_" + SimpleDateFormat("yyyyMMddHHmmss").format(Date(System.currentTimeMillis()))+ ".log", log)
                if (BuildConfig.DEBUG) {
                    oriHandler.uncaughtException(thread, e) //debug模式，默认抛出异常
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取当前进程名
     */
    private fun getCurProcessName(context: Context): String {
        try {
            val pid = Process.myPid()
            val mActivityManager: ActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getCrashInfo(ex: Throwable): String {
        val sb = StringBuffer()
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        return sb.toString()
    }

    /**
     * 发送崩溃日志
     */
    private fun sendExceptionLog(log: String) {
        try {
            val jsonObject = JSONObject(log)
            val keyIter = jsonObject.keys()
            var key: String
            var value: Any
            val valueMap = HashMap<String, Any>()
            while (keyIter.hasNext()) {
                key = keyIter.next()
                value = jsonObject.get(key)
                valueMap[key] = value
            }
            // 把异常信息发送到服务器 

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 获取手机信息
     */
    private fun getPhoneInfo(): String {
        var phoneInfo = "\n Product: " + Build.PRODUCT
        phoneInfo += "\n CPU_ABI: " + Build.SUPPORTED_ABIS.toJson()
        phoneInfo += "\n TAGS: " + Build.TAGS
        phoneInfo += "\n VERSION_CODES.BASE: " + Build.VERSION_CODES.BASE
        phoneInfo += "\n MODEL: " + Build.MODEL
        phoneInfo += "\n SDK: " + Build.VERSION.SDK_INT
        phoneInfo += "\n VERSION.RELEASE: " + Build.VERSION.RELEASE
        phoneInfo += "\n DEVICE: " + Build.DEVICE
        phoneInfo += "\n DISPLAY: " + Build.DISPLAY
        phoneInfo += "\n BRAND: " + Build.BRAND
        phoneInfo += "\n BOARD: " + Build.BOARD
        phoneInfo += "\n FINGERPRINT: " + Build.FINGERPRINT
        phoneInfo += "\n ID: " + Build.ID
        phoneInfo += "\n MANUFACTURER: " + Build.MANUFACTURER
        phoneInfo += "\n USER: " + Build.USER
        return phoneInfo
    }

    private fun saveLogLocal(fileName: String, log: String) {
        try {
            val fos = FileOutputStream(fileName)
            if (log.isNotEmpty()) {
                fos.write(log.toByteArray())
            }
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}