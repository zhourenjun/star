@file:Suppress("PropertyName")

package com.dx.star.model.remote

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import androidx.annotation.IntDef
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

/**
 *
 * java类作用描述
 * zrj 2021/8/11 10:54
 * 更新者 2021/8/11 10:54
 */
open class BaseBean {
    var code = ""
    var msg = ""
    var status = 0
}

data class HttpResult<out T>(@SerializedName("data") val data: T?) : BaseBean()



class DownloadEstimate(
    @SerializedName("Status") val status: String,
    @SerializedName("CourseGrpID") val courseGrpID: String,
    @SerializedName("CasehstUUID") val casehstUUID: String,
    @SerializedName("CureDate") val cureDate: String,
    @SerializedName("CreateDate") val createDate: String,
    @SerializedName("CourseGrpUUID") val courseGrpUUID: String,
    @SerializedName("UpdateTime") val updateTime: String,
    @SerializedName("IsRecommanded") val isRecommanded: String,
    @SerializedName("PatientUUID") val patientUUID: String,
    @SerializedName("IsFinished") val isFinished: String,
)



@Parcelize
data class CheckUpgrade(
    @SerializedName("showVersion") val showVersion: String, //显示版本号
    @SerializedName("changeLog") val changeLog: String, //更新说明
    @SerializedName("fileUrl") val fileUrl: String, //文件地址
    @SerializedName("innerVersion") val innerVersion: Int, //内部版本号
    @SerializedName("upgrade") val upgrade: Int, //是否强制升级(1,启用.2,禁用)
    @SerializedName("state") val state: Int //1,启用.2,禁用
) : Parcelable


data class Device(@DeviceStatus.Project var status: Int, val bluetoothDevice: BluetoothDevice)

class DeviceStatus {
    companion object {
        const val BOND_NONE = 10 //未配对
        const val BONDING = 11 //配对中
        const val BONDED = 12 //已配对
        const val UNCONNECTED = 0 //未连接
        const val CONNECTING = 3 //连接中
        const val CONNECTED = 4 //已连接
        const val DISCONNECTING = 5 //断开中
        const val DISCONNECTED = 6 //已断开
    }

    @IntDef(
        BOND_NONE,
        UNCONNECTED,
        BONDING,
        BONDED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Project
}




class Json() : JSONObject() {
    constructor(json: Json.() -> Unit) : this() {
        this.json()
    }

    infix fun <T> String.to(value: T) {
        put(this, value)
    }
}

data class NameAndTime(val name: String, val time: Int)