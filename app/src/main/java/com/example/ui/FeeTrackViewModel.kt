package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FeeTrackDatabase
import com.example.data.model.ClassCollectionStat
import com.example.data.model.DashboardStats
import com.example.data.model.FeeCategoryBreakdown
import com.example.data.model.FeeStructureEntity
import com.example.data.model.LeaderboardItem
import com.example.data.model.PaymentEntity
import com.example.data.model.PaymentMethodStat
import com.example.data.model.StudentEntity
import com.example.data.repository.FeeTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

sealed class AppScreen {
  data object Login : AppScreen()
  data object Dashboard : AppScreen()
  data object StudentsList : AppScreen()
  data class StudentProfile(val studentId: Long) : AppScreen()
  data class RecordPayment(val preselectedStudentId: Long? = null) : AppScreen()
  data object FeeStructureSetup : AppScreen()
  data object PaymentHistory : AppScreen()
  data object Reports : AppScreen()
}

class FeeTrackViewModel(application: Application) : AndroidViewModel(application) {
  private val database = FeeTrackDatabase.getDatabase(application, viewModelScope)
  private val repository = FeeTrackRepository(
    database.studentDao(),
    database.paymentDao(),
    database.feeStructureDao()
  )

  // Auth / Session State
  private val _isLoggedIn = MutableStateFlow(true) // Pre-authenticated for quick seamless dashboard access
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  val adminName = "Mrs. Sarah Jenkins"
  val adminRole = "Chief Bursar"
  val schoolName = "Oakridge International Academy"
  val academicSession = "2026/2027 • Term 2"

  // Navigation State
  private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Dashboard)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  // Navigation History for back handling
  private val screenStack = mutableListOf<AppScreen>(AppScreen.Dashboard)

  // Data streams
  val allStudents: StateFlow<List<StudentEntity>> = repository.allStudents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val recentPayments: StateFlow<List<PaymentEntity>> = repository.recentPayments
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allPayments: StateFlow<List<PaymentEntity>> = repository.allPayments
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val dashboardStats: StateFlow<DashboardStats> = repository.dashboardStats
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      DashboardStats(0.0, 0.0, 0.0, 0, 0, 0f)
    )

  val classCollectionStats: StateFlow<List<ClassCollectionStat>> = repository.classCollectionStats
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val paymentMethodStats: StateFlow<List<PaymentMethodStat>> = repository.paymentMethodStats
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val leaderboardItems: StateFlow<List<LeaderboardItem>> = repository.leaderboardItems
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Students Screen Filters
  val studentSearchQuery = MutableStateFlow("")
  val studentClassFilter = MutableStateFlow("All")
  val studentStatusFilter = MutableStateFlow("All")

  val filteredStudents: StateFlow<List<StudentEntity>> = combine(
    allStudents,
    studentSearchQuery,
    studentClassFilter,
    studentStatusFilter
  ) { list, query, cls, status ->
    list.filter { student ->
      val matchesQuery = query.isBlank() ||
        student.name.contains(query, ignoreCase = true) ||
        student.studentCode.contains(query, ignoreCase = true) ||
        student.guardianName.contains(query, ignoreCase = true)

      val matchesClass = cls == "All" || student.classLevel.equals(cls, ignoreCase = true)
      val matchesStatus = status == "All" || student.status.equals(status, ignoreCase = true)

      matchesQuery && matchesClass && matchesStatus
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Selected Student Profile State
  private val _selectedStudentId = MutableStateFlow<Long?>(null)
  val selectedStudent: StateFlow<StudentEntity?> = _selectedStudentId.flatMapLatest { id ->
    if (id != null) repository.getStudentById(id) else flowOf(null)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val selectedStudentPayments: StateFlow<List<PaymentEntity>> = _selectedStudentId.flatMapLatest { id ->
    if (id != null) repository.getPaymentsForStudent(id) else flowOf(emptyList())
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val selectedStudentBreakdown: StateFlow<List<FeeCategoryBreakdown>> = _selectedStudentId.flatMapLatest { id ->
    if (id != null) repository.getFeeBreakdownForStudent(id) else flowOf(emptyList())
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Payment History Filters
  val paymentHistorySearch = MutableStateFlow("")
  val paymentHistoryClassFilter = MutableStateFlow("All")
  val paymentHistoryMethodFilter = MutableStateFlow("All")
  val paymentHistoryTermFilter = MutableStateFlow("All")

  val filteredPayments: StateFlow<List<PaymentEntity>> = combine(
    allPayments,
    paymentHistorySearch,
    paymentHistoryClassFilter,
    paymentHistoryMethodFilter,
    paymentHistoryTermFilter
  ) { list, query, cls, method, term ->
    list.filter { payment ->
      val matchesQuery = query.isBlank() ||
        payment.studentName.contains(query, ignoreCase = true) ||
        payment.receiptNumber.contains(query, ignoreCase = true) ||
        payment.referenceNote.contains(query, ignoreCase = true)
      val matchesClass = cls == "All" || payment.classLevel.equals(cls, ignoreCase = true)
      val matchesMethod = method == "All" || payment.paymentMethod.equals(method, ignoreCase = true)
      val matchesTerm = term == "All" || payment.term.equals(term, ignoreCase = true)

      matchesQuery && matchesClass && matchesMethod && matchesTerm
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Record Payment Form State
  val recordPaymentStudentId = MutableStateFlow<Long?>(null)
  val recordPaymentAmount = MutableStateFlow("")
  val recordPaymentMethod = MutableStateFlow("Bank Transfer")
  val recordPaymentCategory = MutableStateFlow("Tuition Fee")
  val recordPaymentTerm = MutableStateFlow("Term 2")
  val recordPaymentDate = MutableStateFlow(
    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
  )
  val recordPaymentReceiptNo = MutableStateFlow(generateReceiptNumber())
  val recordPaymentReference = MutableStateFlow("")
  val paymentSubmissionError = MutableStateFlow<String?>(null)

  // Receipt Modal State
  val activeReceipt = MutableStateFlow<PaymentEntity?>(null)
  val showReceiptModal = MutableStateFlow(false)

  // Fee Structure Setup State
  val feeStructureClass = MutableStateFlow("Grade 10")
  val feeStructureTerm = MutableStateFlow("Term 2")
  val allFeeStructures: StateFlow<List<FeeStructureEntity>> = repository.allFeeStructures
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val currentClassFeeStructures: StateFlow<List<FeeStructureEntity>> = combine(
    allFeeStructures,
    feeStructureClass,
    feeStructureTerm
  ) { structures, cls, term ->
    structures.filter { it.classLevel == cls && it.term == term }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val feeStructureSavedMessage = MutableStateFlow<String?>(null)

  // Export Dialog State
  val showExportDialog = MutableStateFlow(false)
  val exportDataText = MutableStateFlow("")
  val exportTitle = MutableStateFlow("")

  // Reminder Notification Simulation
  val reminderSnackbarMessage = MutableStateFlow<String?>(null)

  init {
    viewModelScope.launch {
      FeeTrackDatabase.populateInitialData(database)
    }
  }

  fun navigateTo(screen: AppScreen) {
    if (screen is AppScreen.StudentProfile) {
      _selectedStudentId.value = screen.studentId
    } else if (screen is AppScreen.RecordPayment) {
      screen.preselectedStudentId?.let { id ->
        recordPaymentStudentId.value = id
        val student = allStudents.value.find { it.id == id }
        if (student != null && student.balance > 0) {
          recordPaymentAmount.value = student.balance.toInt().toString()
        }
      }
    }
    screenStack.add(screen)
    _currentScreen.value = screen
  }

  fun navigateBack(): Boolean {
    if (screenStack.size > 1) {
      screenStack.removeAt(screenStack.lastIndex)
      val previous = screenStack.last()
      _currentScreen.value = previous
      if (previous is AppScreen.StudentProfile) {
        _selectedStudentId.value = previous.studentId
      }
      return true
    }
    return false
  }

  fun login(user: String, pass: String) {
    _isLoggedIn.value = true
    navigateTo(AppScreen.Dashboard)
  }

  fun logout() {
    _isLoggedIn.value = false
    screenStack.clear()
    screenStack.add(AppScreen.Login)
    _currentScreen.value = AppScreen.Login
  }

  fun openStudentProfile(studentId: Long) {
    _selectedStudentId.value = studentId
    navigateTo(AppScreen.StudentProfile(studentId))
  }

  fun preparePaymentForStudent(studentId: Long) {
    recordPaymentStudentId.value = studentId
    val student = allStudents.value.find { it.id == studentId }
    if (student != null && student.balance > 0) {
      recordPaymentAmount.value = student.balance.toInt().toString()
    } else {
      recordPaymentAmount.value = ""
    }
    recordPaymentReceiptNo.value = generateReceiptNumber()
    navigateTo(AppScreen.RecordPayment(studentId))
  }

  fun savePayment() {
    val studentId = recordPaymentStudentId.value
    if (studentId == null) {
      paymentSubmissionError.value = "Please select a student."
      return
    }
    val amountVal = recordPaymentAmount.value.toDoubleOrNull()
    if (amountVal == null || amountVal <= 0.0) {
      paymentSubmissionError.value = "Please enter a valid payment amount."
      return
    }

    viewModelScope.launch {
      val payment = repository.recordPayment(
        studentId = studentId,
        amount = amountVal,
        paymentMethod = recordPaymentMethod.value,
        term = recordPaymentTerm.value,
        category = recordPaymentCategory.value,
        paymentDate = recordPaymentDate.value,
        referenceNote = recordPaymentReference.value,
        customReceiptNumber = recordPaymentReceiptNo.value
      )

      if (payment != null) {
        activeReceipt.value = payment
        showReceiptModal.value = true
        paymentSubmissionError.value = null
        // Reset form
        recordPaymentAmount.value = ""
        recordPaymentReference.value = ""
        recordPaymentReceiptNo.value = generateReceiptNumber()
      }
    }
  }

  fun viewReceipt(payment: PaymentEntity) {
    activeReceipt.value = payment
    showReceiptModal.value = true
  }

  fun closeReceiptModal() {
    showReceiptModal.value = false
  }

  fun saveFeeStructureChanges(items: List<FeeStructureEntity>) {
    viewModelScope.launch {
      items.forEach { item ->
        repository.updateFeeStructureItem(item)
      }
      feeStructureSavedMessage.value = "Fee structure for ${feeStructureClass.value} updated successfully!"
    }
  }

  fun exportPaymentHistory() {
    val payments = filteredPayments.value
    val csv = buildString {
      append("Receipt No,Date,Student Name,Student ID,Class,Category,Method,Amount,Recorded By\n")
      payments.forEach { p ->
        append("\"${p.receiptNumber}\",\"${p.paymentDate}\",\"${p.studentName}\",\"${p.studentCode}\",\"${p.classLevel}\",\"${p.category}\",\"${p.paymentMethod}\",${p.amount},\"${p.recordedBy}\"\n")
      }
    }
    exportTitle.value = "Payment History Export (${payments.size} records)"
    exportDataText.value = csv
    showExportDialog.value = true
  }

  fun exportReportsSummary() {
    val stats = dashboardStats.value
    val classStats = classCollectionStats.value
    val methodStats = paymentMethodStats.value
    val summary = buildString {
      append("FEETRACK EXECUTIVE FINANCIAL REPORT\n")
      append("School: $schoolName\n")
      append("Session: $academicSession\n")
      append("Generated By: $adminName ($adminRole)\n\n")
      append("1. EXECUTIVE SUMMARY\n")
      append("Total Expected Fees: $${String.format(Locale.US, "%,.2f", stats.totalExpected)}\n")
      append("Total Collected: $${String.format(Locale.US, "%,.2f", stats.totalCollected)}\n")
      append("Outstanding Balance: $${String.format(Locale.US, "%,.2f", stats.outstandingBalance)}\n")
      append("Collection Rate: ${String.format(Locale.US, "%.1f", stats.collectionRate)}%\n")
      append("Students in Default: ${stats.studentsInDefault}\n\n")
      append("2. COLLECTION BY CLASS\n")
      classStats.forEach { c ->
        append("${c.classLevel}: Expected $${String.format(Locale.US, "%,.0f", c.expected)} | Collected $${String.format(Locale.US, "%,.0f", c.collected)} (${String.format(Locale.US, "%.1f", c.collectionRate)}%)\n")
      }
      append("\n3. PAYMENT CHANNELS BREAKDOWN\n")
      methodStats.forEach { m ->
        append("${m.method}: $${String.format(Locale.US, "%,.0f", m.totalAmount)} (${m.transactionCount} txns, ${String.format(Locale.US, "%.1f", m.percentage)}%)\n")
      }
    }
    exportTitle.value = "Executive Financial Summary"
    exportDataText.value = summary
    showExportDialog.value = true
  }

  fun sendPaymentReminder(leaderboardItem: LeaderboardItem) {
    reminderSnackbarMessage.value = "Fee reminder dispatch scheduled to ${leaderboardItem.studentName}'s guardian."
  }

  fun clearSnackbarMessage() {
    reminderSnackbarMessage.value = null
    feeStructureSavedMessage.value = null
  }

  private fun generateReceiptNumber(): String {
    return "REC-2026-${Random.nextInt(8100, 9999)}"
  }
}
