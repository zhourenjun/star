package com.dx.star.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import com.dx.star.R
import com.dx.star.base.App

/**
 * Create by: yangc
 * Date: 2021/4/30
 * Describe:
 */
object CommonDialogUtil {

    fun showDialog(
        context: Context, title: CharSequence, subContentView: View, positive: CharSequence,
        negative: CharSequence, positiveListener: ((dialog: Dialog) -> Unit)?,
        negativeListener: ((dialog: Dialog) -> Unit)?
    ): CommonDialog {
        return showDialog(
            context = context,
            title = title,
            subContentView = subContentView,
            positiveBtnText = positive,
            negativeBtnText = negative,
            positiveListener = positiveListener,
            negativeListener = negativeListener,
        )
    }

    fun showDialog(
        context: Context,
        title: CharSequence,
        msg: CharSequence,
        positiveListener: ((dialog: Dialog) -> Unit)?,
        negativeListener: ((dialog: Dialog) -> Unit)?,
    ): CommonDialog {
        return showDialog(
            context,
            title,
            msg,
            App.context.resources.getString(R.string.sure),
            App.context.resources.getString(R.string.cancel),
            positiveListener,
            negativeListener
        )
    }

    fun showDialog(
        context: Context,
        title: CharSequence,
        subContentView: View,
        positiveListener: ((dialog: Dialog) -> Unit)?,
        negativeListener: ((dialog: Dialog) -> Unit)?
    ): CommonDialog {
        return showDialog(
            context = context,
            title = title,
            subContentView = subContentView,
            positiveBtnText = App.context.resources.getString(R.string.sure),
            negativeBtnText = App.context.resources.getString(R.string.cancel),
            positiveListener = positiveListener,
            negativeListener = negativeListener
        )
    }

     fun showDialog(
        context: Context,
        title: CharSequence = "",
        msg: CharSequence = "",
        positiveBtnText: CharSequence = "",
        negativeBtnText: CharSequence = "",
        positiveListener: ((dialog: Dialog) -> Unit)? = null,
        negativeListener: ((dialog: Dialog) -> Unit)? = null,
        subContentView: View? = null,
        contentView: View? = null,
        cancelable: Boolean = true
    ): CommonDialog {
        return CommonDialog.Builder().apply {
            mContext = context
            mSubContentView = subContentView
            mContentView = contentView
            mTitle = title
            mMsg = msg
            mNegativeText = negativeBtnText
            mPositiveText = positiveBtnText
            mNegativeClickListener = negativeListener
            mPositiveClickListener = positiveListener
            mCancelable = cancelable
        }.build()
    }
}