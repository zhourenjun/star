package com.dx.star.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
/**
 *
 * java类作用描述
 * zrj 2021/8/12 15:07
 * 更新者 2021/8/12 15:07
 */
abstract class BindingCommonAdapter<T>(val datas: List<T>) :
    RecyclerView.Adapter<BindingViewHolder<ViewBinding>>() {
    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewBinding> {
        context = parent.context
        return createBindingViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) {
        bindData(holder, position, datas[position], getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    abstract fun createBindingViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<ViewBinding>

    //数据绑定
    abstract fun bindData(
        viewHolder: BindingViewHolder<ViewBinding>,
        position: Int,
        data: T,
        itemType: Int
    )
}


class BindingViewHolder<VB:ViewBinding>(val binding:VB): RecyclerView.ViewHolder(binding.root){
}

inline fun <reified T : ViewBinding> newBindingViewHolder(parent: ViewGroup): BindingViewHolder<ViewBinding> {
    val method = T::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as T
    return BindingViewHolder(binding)
}