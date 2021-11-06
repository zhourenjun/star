package com.dx.star.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import com.dx.star.base.App

/**
 * Created by ljc.
 */
object ToastUtil {

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    fun showMsg(context: Context?, msg: String) {
        try {

            if (context != null && mToast == null) {
                mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
            }
            mToast?.setGravity(Gravity.CENTER, 0, 0)
            mToast?.setText(msg)
            mToast?.show()
        } catch (e: Exception) {
            //在子线程中调用Toast的异常处理
            Looper.prepare()
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }
    }

    @SuppressLint("ShowToast")
    fun showMsg(context: Context?, msgId: Int) {
        context?.getString(msgId)?.let { showMsg(context, it) }
    }

    fun showMsg(msg: String) {
        showMsg(App.context, msg)
    }

    fun showMsg(msg: Int) {
        showMsg(
            App.context,
            App.context.resources.getString(msg)
        )
    }
}