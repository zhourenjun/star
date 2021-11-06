package com.creative.recure.model.local

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

/**
 *
 * java类作用描述
 * zrj 2021/8/11 11:45
 * 更新者 2021/8/11 11:45
 */

@Dao
interface NewPatientDao {
    @Query("SELECT * FROM new_patient_table ORDER BY createDate DESC") //ACS
     fun getAllPatients(): Flow<List<NewPatient>>

    @Transaction
    @Query("SELECT * FROM new_patient_table WHERE patientName LIKE '%' || :search || '%' or userCode LIKE '%' || :search || '%' or phone LIKE '%' || :search || '%'")
     fun getPatientSearch(search: String): Flow<List<NewPatient>>

    @Query("SELECT * FROM new_patient_table WHERE patientUUID = :patientUUID")
    suspend fun getPatient(patientUUID: String): NewPatient?

    @Query("SELECT EXISTS(SELECT 1 FROM new_patient_table WHERE phone = :phone LIMIT 1)")
    suspend fun getPatientByPhone(phone: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM new_patient_table WHERE identityNo = :identityNo LIMIT 1)")
    suspend fun getPatientByIdentityNo(identityNo: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM new_patient_table WHERE medicareNo = :medicareNo LIMIT 1)")
    suspend fun getPatientByMedicareNo(medicareNo: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM new_patient_table WHERE barCodeNum = :barCodeNum LIMIT 1)")
    suspend fun getPatientByBarCodeNum(barCodeNum: String): Boolean

    @Query("SELECT * FROM new_patient_table WHERE isUpload = 0")
    suspend fun getPatientByUpload(): List<NewPatient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patients: List<NewPatient>)

    @Update
    suspend fun update(patients: List<NewPatient>)

    @Query("DELETE FROM new_patient_table where patientUUID in (:idList)")
    suspend fun deletePatientById(idList: List<String>)
}

@Parcelize
@Entity(tableName = "new_patient_table")
class NewPatient: Parcelable {
    @NonNull
    @SerializedName("PatientUUID")
    @PrimaryKey
    var patientUUID: String = ""
    @SerializedName("PatientNumber") //病历号  后端使用
    var userCode: String= ""
    @SerializedName("PatientName")
    var patientName: String= ""
    @SerializedName("Phone")
    var phone: String= ""
    @SerializedName("Sex")
    var sex: String= ""
    @SerializedName("Age")
    var age: Int= 0
    @SerializedName("Birthday")
    var birthday: String= ""
    @SerializedName("IdentityNo") //身份证
    var identityNo: String= ""
    @SerializedName("MedicareNo") //社保号
    var medicareNo: String= ""
    @SerializedName("Address")
    var address: String= ""
    @SerializedName("CreateDate")
    var createDate: String= ""
    @SerializedName("UpdateTime")
    var updateTime: String= ""
    @SerializedName("Mark")
    var mark: String= ""
    @SerializedName("BarCodeNum") //探头码
    var barCodeNum: String= ""
    @Ignore
    var isSelected: Boolean = false

    var isUpload = false
}