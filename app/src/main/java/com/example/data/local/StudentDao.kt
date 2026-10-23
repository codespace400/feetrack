package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
  @Query("SELECT * FROM students ORDER BY name ASC")
  fun getAllStudentsFlow(): Flow<List<StudentEntity>>

  @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
  fun getStudentByIdFlow(studentId: Long): Flow<StudentEntity?>

  @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
  suspend fun getStudentById(studentId: Long): StudentEntity?

  @Query("SELECT * FROM students WHERE classLevel = :classLevel ORDER BY name ASC")
  fun getStudentsByClassFlow(classLevel: String): Flow<List<StudentEntity>>

  @Query("SELECT * FROM students WHERE status = :status ORDER BY balance DESC")
  fun getStudentsByStatusFlow(status: String): Flow<List<StudentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudent(student: StudentEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudents(students: List<StudentEntity>)

  @Update
  suspend fun updateStudent(student: StudentEntity)

  @Query("SELECT COUNT(*) FROM students")
  suspend fun getCount(): Int

  @Query("SELECT SUM(totalFee) FROM students")
  fun getTotalExpectedFeeFlow(): Flow<Double?>

  @Query("SELECT SUM(amountPaid) FROM students")
  fun getTotalCollectedFeeFlow(): Flow<Double?>

  @Query("SELECT SUM(balance) FROM students")
  fun getTotalOutstandingBalanceFlow(): Flow<Double?>

  @Query("SELECT COUNT(*) FROM students WHERE status = 'OWING'")
  fun getOwingStudentsCountFlow(): Flow<Int>
}
