package com.dx.star.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * java类作用描述
 * zrj 2021/8/12 9:42
 * 更新者 2021/8/12 9:42
 */
object DateFormatUtil {


    private fun getDefaultSimpleDateFormat(pattern: String): SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT+08")
        }
    }

    /**
     * 将时间戳转为时间字符串
     * 格式为yyyy-MM-dd HH:mm:ss
     */
    fun milliseconds2String(milliseconds: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        return getDefaultSimpleDateFormat(pattern).format(Date(milliseconds))
    }

    fun string2Milliseconds(time: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Long {
        try {
            return getDefaultSimpleDateFormat(pattern).parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return -1
    }

    fun getCurTimeString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
        getDefaultSimpleDateFormat(pattern).format(Date())


    fun isNeedUpdate(v1: String, v2: String): Boolean {// v1 新进  v2 本地
        if (v1.isEmpty()) return false
        if (v2.isEmpty()) return true
        return string2Milliseconds(v1) > string2Milliseconds(v2)
    }

}