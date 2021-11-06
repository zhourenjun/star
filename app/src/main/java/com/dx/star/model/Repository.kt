package com.dx.star.model

import com.dx.star.base.BaseRepository
import com.dx.star.model.local.AppDatabase
import com.dx.star.model.remote.Api

/**
 *
 * 推荐处方数据处理
 * zrj 2021/6/26 10:55
 * 更新者 2021/6/26 10:55
 */
class Repository(private val db: AppDatabase, private val api: Api) : BaseRepository() {


    //-----------------------------------------病人--------------------------------------
    suspend fun isPatientExists(patientId: String) = db.patientDao().getPatient(patientId) != null

    suspend fun getPatientByPhone(phone: String) = db.patientDao().getPatientByPhone(phone)

    suspend fun getPatientByIdentityNo(identityNo: String) =
        db.patientDao().getPatientByIdentityNo(identityNo)

    suspend fun getPatientByMedicareNo(medicareNo: String) =
        db.patientDao().getPatientByMedicareNo(medicareNo)

    suspend fun getPatientByBarCodeNum(barCodeNum: String) =
        db.patientDao().getPatientByBarCodeNum(barCodeNum)

    suspend fun getPatientByUpload() = db.patientDao().getPatientByUpload()

    fun getAllPatients() = db.patientDao().getAllPatients()

    fun getPatients(search: String) = db.patientDao().getPatientSearch(search)




//    suspend fun uploadSetting(updateTime: String, file: File): HttpResult<Any> {
//        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//        val filePart = MultipartBody.Part.createFormData("zipFile", file.name, requestBody)
//        val imei = sp().getString(Constant.IMEI, "") ?: ""
//        return apiCall { api.uploadSetting(imei, updateTime, filePart) }
//    }



}

