package com.dx.star.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.dx.star.R
import com.dx.star.base.bindView
import com.dx.star.databinding.DialogCommonBinding
import com.dx.star.ext.setVisible

/**
 * Create by: yangc
 * Date: 2021/4/30
 * Describe:通用dialog
 */
class CommonDialog private constructor() : DialogFragment() {
    lateinit var mContext: Context
    private var mTitle: CharSequence = ""
    private var mMsg: CharSequence = ""
    private var mSubContentView: View? = null
    private var mContentView: View? = null
    private var mPositiveText: CharSequence = ""
    private var mNegativeText: CharSequence = ""
    private var mPositiveClickListener: ((dialog: Dialog) -> Unit)? = null
    private var mNegativeClickListener: ((dialog: Dialog) -> Unit)? = null
    private val binding: DialogCommonBinding by bindView()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = Dialog(mContext, R.style.dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (mContentView != null) {
            mContentView
        } else {
            layoutInflater.inflate(R.layout.dialog_common, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setWindowAttrs()
    }

    private fun setWindowAttrs() {
        val dialog = dialog
        if (dialog != null && dialog.window != null) {
            val window = dialog.window
            if (window != null) {
                val attrs = window.attributes
                //设置宽度
                attrs.width = 800f.dip2px(mContext)
                attrs.height = 408f.dip2px(mContext)
                //设置dialog显示的位置
                window.setGravity(Gravity.CENTER)
                window.attributes = attrs
            }
        }
    }

    private fun initView() {
        if (mContentView != null) return
        binding.commonTitle.text = mTitle
        binding.commonMsg.text = mMsg
        setContentViewById()
        setNegative()
        setPositive()
    }

    private fun setContentViewById() {
        if (mSubContentView != null) {
            binding.commonMsg.setVisible(false)
            binding.commonFl.addView(mSubContentView)
        }
    }

    private fun setPositive() {
        if (mPositiveText.isEmpty()) {
            binding.commonPositive.setVisible(false)
        } else {
            binding.commonPositive.text = mPositiveText
        }
        binding.commonPositive.setOnClickListener {
            val mDialog = dialog
            val listener = mPositiveClickListener
            if (listener != null && mDialog != null) {
                listener.invoke(mDialog)
            }
        }
    }

    private fun setNegative() {
        if (mNegativeText.isEmpty()) {
            binding.commonNegative.setVisible(false)
        } else {
            binding.commonNegative.text = mNegativeText
        }
        binding.commonNegative.setOnClickListener {
            val mDialog = dialog
            val listener = mNegativeClickListener
            if (listener != null && mDialog != null) {
                listener.invoke(mDialog)
            }
        }
    }

    private var onDismissListener: (() -> Unit)? = null
    fun setOnDismissListener(l: (() -> Unit)) {
        this.onDismissListener = l
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }
    /**
     * 显示Dialog
     */
    fun show() {
        try {
            if (mContext is FragmentActivity) {
                val fragmentActivity = mContext as FragmentActivity
                if (fragmentActivity.isFinishing || fragmentActivity.isDestroyed) {
                    return
                }
                if (!isAdded) {
                    show(fragmentActivity.supportFragmentManager, CommonDialog::class.java.name)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Builder {
        lateinit var mContext: Context

        /*
        * 标题
        * */
        var mTitle: CharSequence = ""

        /*
        * 内容消息
        * */
        var mMsg: CharSequence = ""

        /*
        * 内容子view
        * */
        var mSubContentView: View? = null

        /*
        * 内容，不为空则只展示该view
        * */
        var mContentView: View? = null

        /*
        * 左边按钮
        * */
        var mPositiveText: CharSequence = ""

        /*
        * 右边按钮
        * */
        var mNegativeText: CharSequence = ""
        var mPositiveClickListener: ((dialog: Dialog) -> Unit)? = null
        var mNegativeClickListener: ((dialog: Dialog) -> Unit)? = null
        var mCancelable = true
        fun build(): CommonDialog {
            val dialog = CommonDialog()
            dialog.mContext = mContext
            dialog.mTitle = mTitle
            dialog.mMsg = mMsg
            dialog.mSubContentView = mSubContentView
            dialog.mContentView = mContentView
            dialog.mPositiveText = mPositiveText
            dialog.mNegativeText = mNegativeText
            dialog.mPositiveClickListener = mPositiveClickListener
            dialog.mNegativeClickListener = mNegativeClickListener
            dialog.isCancelable = mCancelable
            return dialog
        }
    }

}