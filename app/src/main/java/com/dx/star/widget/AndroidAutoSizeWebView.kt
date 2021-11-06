package com.dx.star.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import me.jessyan.autosize.AutoSize

/**
 * 自定义WebView，适配AndroidAutoSize，解决返回适配失效的问题。
 */
class AndroidAutoSizeWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setOverScrollMode(mode: Int) {
        super.setOverScrollMode(mode)
        AutoSize.autoConvertDensityOfGlobal(context as Activity)
    }
}