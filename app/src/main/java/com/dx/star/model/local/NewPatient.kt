package com.dx.star.model.local

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

/**
 *
 * java类作用描述
 * zrj 2021/8/11 11:45
 * 更新者 2021/8/11 11:45
 */

@Dao
interface NewPatientDao {

    @Query("SELECT * FROM new_patient_table WHERE patientUUID = :patientUUID")
    suspend fun getPatient(patientUUID: String): NewPatient?

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
    @PrimaryKey
    var patientUUID: String = ""
    var userCode: String= ""
    var patientName: String= ""
    var phone: String= ""
    var sex: String= ""
    var age: Int= 0
    var birthday: String= ""
    @Ignore
    var isSelected: Boolean = false
    var isUpload = false
}