package com.dx.star.ext

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

/**
 *
 * java类作用描述
 * zrj 2021/8/11 19:17
 * 更新者 2021/8/11 19:17
 */
fun View.setVisible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setInVisible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

var triggerLastTime = 0L
var triggerDelay = 600L

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
        triggerLastTime = currentClickTime
    }
    return flag
}


@Suppress("UNCHECKED_CAST")
internal fun <T : View> T.singleClick(click: (view: T) -> Unit) {
    setOnClickListener {
        if (clickEnable()) {
            click(it as T)
        }
    }
}


fun EditText.setTextChangeListener(body: (key: String) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            body(s.toString())
        }
    })
}

fun Spinner.onItemSelectedListener(
    isFirstShow: Boolean,
    body: (position: Int, isChange: Boolean) -> Unit
) {
    var isShow = isFirstShow
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (isShow) {
                view?.setVisible(true)
                body(position, true)
            } else {//初始值不显示
                view?.setVisible(false)
                body(position, false)
                isShow = true
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}

fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun CheckBox.getString() = if (this.isChecked) "${this.text}  " else ""


fun getTextHeight(paint: Paint, text: String): Int {
    val rect = Rect()
    paint.getTextBounds(text, 0, text.length, rect)
    return rect.height()
}


fun View.setEnableAndChangeAlpha(isEnable: Boolean) {
    this.isEnabled = isEnabled
    this.alpha = if (isEnable) 1f else 0.3f
}


fun View.forEachChildView(closure: (View) -> Unit) {
    closure(this)
    val groupView = this as? ViewGroup ?: return
    val size = groupView.childCount - 1
    for (i in 0..size) {
        groupView.getChildAt(i).forEachChildView(closure)
    }
}

fun CheckBox.getInt() = if (this.isChecked) 1 else 0

inline fun TabLayout.addOnTabChangeListener(
    crossinline onTabUnselected: (tab: TabLayout.Tab?) -> Unit = { _ -> },
    crossinline onTabReselected: (tab: TabLayout.Tab?) -> Unit = { _ -> },
    crossinline onTabSelected: (tab: TabLayout.Tab?) -> Unit = { _ -> }
): TabLayout.OnTabSelectedListener {
    val tabChangeListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            onTabReselected.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onTabUnselected.invoke(tab)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onTabSelected.invoke(tab)
        }
    }
    addOnTabSelectedListener(tabChangeListener)
    return tabChangeListener
}