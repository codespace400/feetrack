package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
  @Query("SELECT * FROM payments ORDER BY id DESC")
  fun getAllPaymentsFlow(): Flow<List<PaymentEntity>>

  @Query("SELECT * FROM payments ORDER BY id DESC LIMIT :limit")
  fun getRecentPaymentsFlow(limit: Int = 10): Flow<List<PaymentEntity>>

  @Query("SELECT * FROM payments WHERE studentId = :studentId ORDER BY id DESC")
  fun getPaymentsForStudentFlow(studentId: Long): Flow<List<PaymentEntity>>

  @Query("SELECT * FROM payments WHERE classLevel = :classLevel ORDER BY id DESC")
  fun getPaymentsForClassFlow(classLevel: String): Flow<List<PaymentEntity>>

  @Query("SELECT * FROM payments WHERE receiptNumber = :receiptNumber LIMIT 1")
  suspend fun getPaymentByReceipt(receiptNumber: String): PaymentEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayment(payment: PaymentEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayments(payments: List<PaymentEntity>)

  @Query("SELECT COUNT(*) FROM payments")
  suspend fun getCount(): Int

  @Query("SELECT SUM(amount) FROM payments")
  fun getTotalPaymentAmountFlow(): Flow<Double?>
}
