package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val studentCode: String,
  val name: String,
  val classLevel: String, // e.g. "Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12"
  val gender: String, // "M", "F"
  val guardianName: String,
  val guardianPhone: String,
  val guardianEmail: String,
  val totalFee: Double,
  val amountPaid: Double,
  val balance: Double,
  val status: String, // "PAID", "PARTIAL", "OWING"
  val academicYear: String = "2026/2027",
  val term: String = "Term 2",
)

@Entity(tableName = "payments")
data class PaymentEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val receiptNumber: String,
  val studentId: Long,
  val studentCode: String,
  val studentName: String,
  val classLevel: String,
  val amount: Double,
  val paymentMethod: String, // "Bank Transfer", "Cash", "Card / POS", "Cheque"
  val paymentDate: String,
  val term: String, // "Term 1", "Term 2", "Term 3"
  val category: String, // "Tuition", "Books & Stationery", "School Uniform", "Science & ICT Lab", "Sports & Activities"
  val referenceNote: String,
  val recordedBy: String = "Sarah Jenkins (Chief Bursar)",
)

@Entity(tableName = "fee_structures")
data class FeeStructureEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val classLevel: String,
  val term: String,
  val category: String,
  val amount: Double,
  val isMandatory: Boolean = true,
)

data class FeeCategoryBreakdown(
  val category: String,
  val term: String,
  val billedAmount: Double,
  val paidAmount: Double,
  val balance: Double,
)

data class DashboardStats(
  val totalExpected: Double,
  val totalCollected: Double,
  val outstandingBalance: Double,
  val studentsInDefault: Int,
  val totalStudents: Int,
  val collectionRate: Float,
)

data class ClassCollectionStat(
  val classLevel: String,
  val expected: Double,
  val collected: Double,
  val outstanding: Double,
  val collectionRate: Float,
)

data class PaymentMethodStat(
  val method: String,
  val totalAmount: Double,
  val transactionCount: Int,
  val percentage: Float,
)

data class LeaderboardItem(
  val rank: Int,
  val studentId: Long,
  val studentName: String,
  val studentCode: String,
  val classLevel: String,
  val balance: Double,
  val daysOverdue: Int,
)

enum class FeeStatus(val displayName: String) {
  PAID("Paid"),
  PARTIAL("Partial"),
  OWING("Owing")
}
