package com.dx.star.model.remote

import com.creative.recure.model.remote.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 *
 * java类作用描述
 * zrj 2021/8/11 10:58
 * 更新者 2021/8/11 10:58
 */
interface Api {

    //上传设置
    @Multipart
    @POST("/systemConfiguration/upload")
    suspend fun uploadSetting(
        @Part("imei") imei: String,
        @Part("updateTime") updateTime: String,
        @Part file: MultipartBody.Part
    ): HttpResult<Any>

    //上传问卷
    @Multipart
    @POST("/patientInfo/uploadPatientQuestionnaire")
    suspend fun uploadQuestionnaire(
        @Part("imei") imei: String,
        @Part("questionnaireId") questionnaireId: String,
        @Part("title") title: String,
        @Part("patientId") patientId: String,
        @Part("version") version: String,
        @Part("createTime") createTime: String,
        @Part("updateTime") updateTime: String,
        @Part file: MultipartBody.Part
    ): HttpResult<String>

    //上传评估与处方镜像zip
    @Multipart
    @POST
    suspend fun uploadZip(
        @Url url: String,
        @Part file: MultipartBody.Part,
        @Part("imei") imei: String,
    ): HttpResult<Any>

    //上传查体
    @POST("/patientInfo/physicalexamUpload")
    suspend fun uploadPop(@Body jsonObject: JsonObject): HttpResult<String>

    //上传病档
    @POST("/patientInfo/uploadData/v2")
    @FormUrlEncoded
    suspend fun uploadPatient(@Field("file") file: String): HttpResult<Any>




    @POST("/check/barCodeNum")
    suspend fun checkBarCode(@Body jsonObject: JsonObject): HttpResult<String>
}