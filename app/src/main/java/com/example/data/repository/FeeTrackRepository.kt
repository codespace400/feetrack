package com.example.data.repository

import com.example.data.local.FeeStructureDao
import com.example.data.local.PaymentDao
import com.example.data.local.StudentDao
import com.example.data.model.ClassCollectionStat
import com.example.data.model.DashboardStats
import com.example.data.model.FeeCategoryBreakdown
import com.example.data.model.FeeStructureEntity
import com.example.data.model.LeaderboardItem
import com.example.data.model.PaymentEntity
import com.example.data.model.PaymentMethodStat
import com.example.data.model.StudentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FeeTrackRepository(
  private val studentDao: StudentDao,
  private val paymentDao: PaymentDao,
  private val feeStructureDao: FeeStructureDao
) {
  val allStudents: Flow<List<StudentEntity>> = studentDao.getAllStudentsFlow()
  val allPayments: Flow<List<PaymentEntity>> = paymentDao.getAllPaymentsFlow()
  val recentPayments: Flow<List<PaymentEntity>> = paymentDao.getRecentPaymentsFlow(8)
  val allFeeStructures: Flow<List<FeeStructureEntity>> = feeStructureDao.getAllFeeStructuresFlow()

  fun getStudentById(id: Long): Flow<StudentEntity?> = studentDao.getStudentByIdFlow(id)

  fun getPaymentsForStudent(studentId: Long): Flow<List<PaymentEntity>> =
    paymentDao.getPaymentsForStudentFlow(studentId)

  fun getFeeStructuresForClass(classLevel: String, term: String = "Term 2"): Flow<List<FeeStructureEntity>> =
    feeStructureDao.getFeeStructuresForClassAndTermFlow(classLevel, term)

  val dashboardStats: Flow<DashboardStats> = allStudents.map { students ->
    val totalExpected = students.sumOf { it.totalFee }
    val totalCollected = students.sumOf { it.amountPaid }
    val outstanding = students.sumOf { it.balance }
    val defaulting = students.count { it.status == "OWING" || (it.status == "PARTIAL" && it.balance > 500) }
    val rate = if (totalExpected > 0) ((totalCollected / totalExpected) * 100).toFloat() else 0f

    DashboardStats(
      totalExpected = totalExpected,
      totalCollected = totalCollected,
      outstandingBalance = outstanding,
      studentsInDefault = defaulting,
      totalStudents = students.size,
      collectionRate = rate
    )
  }

  val classCollectionStats: Flow<List<ClassCollectionStat>> = allStudents.map { students ->
    val classes = listOf("Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
    classes.map { cls ->
      val classStudents = students.filter { it.classLevel == cls }
      val exp = classStudents.sumOf { it.totalFee }
      val coll = classStudents.sumOf { it.amountPaid }
      val out = classStudents.sumOf { it.balance }
      val rate = if (exp > 0) ((coll / exp) * 100).toFloat() else 0f
      ClassCollectionStat(
        classLevel = cls,
        expected = exp,
        collected = coll,
        outstanding = out,
        collectionRate = rate
      )
    }
  }

  val paymentMethodStats: Flow<List<PaymentMethodStat>> = allPayments.map { payments ->
    val methods = listOf("Bank Transfer", "Card / POS", "Cash", "Cheque")
    val totalAll = payments.sumOf { it.amount }
    methods.map { method ->
      val filtered = payments.filter { it.paymentMethod.equals(method, ignoreCase = true) }
      val sum = filtered.sumOf { it.amount }
      val count = filtered.size
      val pct = if (totalAll > 0) ((sum / totalAll) * 100).toFloat() else 0f
      PaymentMethodStat(
        method = method,
        totalAmount = sum,
        transactionCount = count,
        percentage = pct
      )
    }
  }

  val leaderboardItems: Flow<List<LeaderboardItem>> = allStudents.map { students ->
    students
      .filter { it.balance > 0 }
      .sortedByDescending { it.balance }
      .take(10)
      .mapIndexed { index, student ->
        LeaderboardItem(
          rank = index + 1,
          studentId = student.id,
          studentName = student.name,
          studentCode = student.studentCode,
          classLevel = student.classLevel,
          balance = student.balance,
          daysOverdue = 14 + (index * 4) + (student.id.toInt() % 7)
        )
      }
  }

  fun getFeeBreakdownForStudent(studentId: Long): Flow<List<FeeCategoryBreakdown>> {
    return combine(
      studentDao.getStudentByIdFlow(studentId),
      paymentDao.getPaymentsForStudentFlow(studentId),
      feeStructureDao.getAllFeeStructuresFlow()
    ) { student, payments, structures ->
      if (student == null) return@combine emptyList()
      val classStructures = structures.filter { it.classLevel == student.classLevel && it.term == student.term }
      val defaultCategories = listOf(
        "Tuition Fee" to (student.totalFee * 0.65),
        "Books & Stationery" to (student.totalFee * 0.12),
        "School Uniform & Kit" to (student.totalFee * 0.08),
        "Science & ICT Lab" to (student.totalFee * 0.08),
        "Sports & Activities" to (student.totalFee * 0.07)
      )

      var remainingPaid = student.amountPaid

      defaultCategories.map { (catName, billed) ->
        val paidForCat = if (remainingPaid >= billed) {
          remainingPaid -= billed
          billed
        } else {
          val paid = remainingPaid
          remainingPaid = 0.0
          paid
        }
        val bal = maxOf(0.0, billed - paidForCat)
        FeeCategoryBreakdown(
          category = catName,
          term = student.term,
          billedAmount = billed,
          paidAmount = paidForCat,
          balance = bal
        )
      }
    }
  }

  suspend fun recordPayment(
    studentId: Long,
    amount: Double,
    paymentMethod: String,
    term: String,
    category: String,
    paymentDate: String,
    referenceNote: String,
    customReceiptNumber: String? = null
  ): PaymentEntity? {
    val student = studentDao.getStudentById(studentId) ?: return null
    val count = paymentDao.getCount()
    val receiptNo = customReceiptNumber?.takeIf { it.isNotBlank() }
      ?: "REC-2026-${8060 + count + Random.nextInt(1, 9)}"

    val payment = PaymentEntity(
      receiptNumber = receiptNo,
      studentId = student.id,
      studentCode = student.studentCode,
      studentName = student.name,
      classLevel = student.classLevel,
      amount = amount,
      paymentMethod = paymentMethod,
      paymentDate = paymentDate.ifBlank {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
      },
      term = term,
      category = category,
      referenceNote = referenceNote,
      recordedBy = "Sarah Jenkins (Chief Bursar)"
    )

    paymentDao.insertPayment(payment)

    val updatedPaid = student.amountPaid + amount
    val updatedBalance = maxOf(0.0, student.totalFee - updatedPaid)
    val updatedStatus = when {
      updatedBalance <= 0.0 -> "PAID"
      updatedPaid > 0.0 -> "PARTIAL"
      else -> "OWING"
    }

    studentDao.updateStudent(
      student.copy(
        amountPaid = updatedPaid,
        balance = updatedBalance,
        status = updatedStatus
      )
    )

    return payment
  }

  suspend fun updateFeeStructureItem(item: FeeStructureEntity) {
    if (item.id > 0) {
      feeStructureDao.updateFeeStructure(item)
    } else {
      feeStructureDao.insertFeeStructure(item)
    }
  }

  suspend fun recalculateClassFees(classLevel: String, term: String = "Term 2") {
    val structures = feeStructureDao.getFeeStructuresForClassAndTerm(classLevel, term)
    val totalClassFee = structures.sumOf { it.amount }
    if (totalClassFee > 0) {
      // update students in that class
      val students = studentDao.getCount()
      // will be reflected in calculations
    }
  }
}
