package com.dx.star.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.RotateDrawable
import android.os.SystemClock
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.FloatRange
import androidx.annotation.MainThread
import androidx.appcompat.content.res.AppCompatResources
import com.dx.star.R

class LoadingForView(
    context: Context,
    private val viewGroup: ViewGroup,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 0f,
    private val minDuration: Int = 100
) {
    private var start: Long = 0
    private val loading = FrameLayout(context)

    var listener: ILoadingListener? = null

    init {
        val view = ProgressBar(context)
        view.indeterminateDrawable = getLoadingDrawable(context)
        val dp120 = 120f.dip2px(context)
        view.layoutParams = FrameLayout.LayoutParams(dp120, dp120).run {
            gravity = Gravity.CENTER
            this
        }
        loading.addView(view)
        loading.setBackgroundColor(Color.argb((255 * alpha).toInt(), 0, 0, 0))
        loading.isEnabled = true
        loading.isClickable = true
        loading.isFocusable = true
        loading.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    @MainThread
    fun show() {
        if (viewGroup.indexOfChild(loading) == -1) {
            viewGroup.addView(loading)
        }
        start = SystemClock.uptimeMillis()
        listener?.onLoadingShowing(this)
    }

    @MainThread
    fun dismiss(arg: Any? = null) {
        val duration = SystemClock.uptimeMillis() - start
        if (duration >= minDuration) {
            viewGroup.removeView(loading)
            listener?.onLoadingDismissed(this, arg)
        } else {
            viewGroup.postDelayed({
                viewGroup.removeView(loading)
                listener?.onLoadingDismissed(this, arg)
            }, minDuration - duration)
        }
    }

    @MainThread
    fun dismissImmediately(arg: Any? = null) {
        viewGroup.removeView(loading)
        listener?.onLoadingDismissed(this, arg)
    }

    private fun getLoadingDrawable(context: Context): RotateDrawable {
        val drawable = RotateDrawable()
        drawable.fromDegrees = 0f
        drawable.toDegrees = 360f
        drawable.pivotX = 0.5f
        drawable.pivotY = 0.5f
        drawable.drawable = AppCompatResources.getDrawable(context, R.drawable.ic_loading)
        return drawable
    }
}

interface ILoadingListener {
    fun onLoadingShowing(loading: LoadingForView)

    fun onLoadingDismissed(loading: LoadingForView, arg: Any? = null) {}
}

fun Float.dip2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}