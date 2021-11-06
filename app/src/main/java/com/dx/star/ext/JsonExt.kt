package com.dx.star.ext

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 *
 * java类作用描述
 * zrj 2021/8/11 11:30
 * 更新者 2021/8/11 11:30
 */
fun Any.toJson(dateFormat: String = "yyyy-MM-dd HH:mm:ss", lenient: Boolean = false, excludeFields: List<String>? = null)
        = GsonBuilder().setDateFormat(dateFormat)
    .apply {
        if(lenient) setLenient()
        if(!excludeFields.isNullOrEmpty()){
            setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes?): Boolean {
                    return f!=null && excludeFields.contains(f.name)
                }
                override fun shouldSkipClass(clazz: Class<*>?) = false
            })
        }
    }
    .create().toJson(this)

inline fun <reified T> String.toBean(dateFormat: String = "yyyy-MM-dd HH:mm:ss", lenient: Boolean = false)
        = GsonBuilder().setDateFormat(dateFormat)
    .apply {
        if(lenient) setLenient()
    }.create()
    .fromJson<T>(this, object : TypeToken<T>() {}.type)