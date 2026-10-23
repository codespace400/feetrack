package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ExportDialog
import com.example.ui.components.FeeTrackSidebar
import com.example.ui.components.ReceiptDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FeeStructureScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PaymentHistoryScreen
import com.example.ui.screens.RecordPaymentScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.screens.StudentsListScreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.PrimaryTealLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeeTrackApp(
  viewModel: FeeTrackViewModel,
  modifier: Modifier = Modifier
) {
  val isLoggedIn by viewModel.isLoggedIn.collectAsState()
  val currentScreen by viewModel.currentScreen.collectAsState()

  val dashboardStats by viewModel.dashboardStats.collectAsState()
  val recentPayments by viewModel.recentPayments.collectAsState()
  val allStudents by viewModel.allStudents.collectAsState()
  val filteredStudents by viewModel.filteredStudents.collectAsState()
  val studentSearch by viewModel.studentSearchQuery.collectAsState()
  val studentClass by viewModel.studentClassFilter.collectAsState()
  val studentStatus by viewModel.studentStatusFilter.collectAsState()

  val selectedStudent by viewModel.selectedStudent.collectAsState()
  val selectedBreakdown by viewModel.selectedStudentBreakdown.collectAsState()
  val selectedPayments by viewModel.selectedStudentPayments.collectAsState()

  val filteredPayments by viewModel.filteredPayments.collectAsState()
  val paymentSearch by viewModel.paymentHistorySearch.collectAsState()
  val paymentClass by viewModel.paymentHistoryClassFilter.collectAsState()
  val paymentMethod by viewModel.paymentHistoryMethodFilter.collectAsState()
  val paymentTerm by viewModel.paymentHistoryTermFilter.collectAsState()

  val recordStudentId by viewModel.recordPaymentStudentId.collectAsState()
  val recordAmount by viewModel.recordPaymentAmount.collectAsState()
  val recordMethod by viewModel.recordPaymentMethod.collectAsState()
  val recordCategory by viewModel.recordPaymentCategory.collectAsState()
  val recordTerm by viewModel.recordPaymentTerm.collectAsState()
  val recordDate by viewModel.recordPaymentDate.collectAsState()
  val recordReceiptNo by viewModel.recordPaymentReceiptNo.collectAsState()
  val recordReference by viewModel.recordPaymentReference.collectAsState()
  val paymentError by viewModel.paymentSubmissionError.collectAsState()

  val feeClass by viewModel.feeStructureClass.collectAsState()
  val feeTerm by viewModel.feeStructureTerm.collectAsState()
  val currentClassStructures by viewModel.currentClassFeeStructures.collectAsState()
  val feeSavedMsg by viewModel.feeStructureSavedMessage.collectAsState()

  val classStats by viewModel.classCollectionStats.collectAsState()
  val paymentMethodStats by viewModel.paymentMethodStats.collectAsState()
  val leaderboardItems by viewModel.leaderboardItems.collectAsState()

  val activeReceipt by viewModel.activeReceipt.collectAsState()
  val showReceiptModal by viewModel.showReceiptModal.collectAsState()

  val showExport by viewModel.showExportDialog.collectAsState()
  val exportTitle by viewModel.exportTitle.collectAsState()
  val exportData by viewModel.exportDataText.collectAsState()

  val snackbarMsg by viewModel.reminderSnackbarMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

  // Handle Snackbars
  LaunchedEffect(snackbarMsg) {
    snackbarMsg?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearSnackbarMessage()
    }
  }

  LaunchedEffect(feeSavedMsg) {
    feeSavedMsg?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearSnackbarMessage()
    }
  }

  // Back handling
  BackHandler(enabled = currentScreen !is AppScreen.Dashboard && currentScreen !is AppScreen.Login) {
    viewModel.navigateBack()
  }

  // Not logged in -> Show LoginScreen
  if (!isLoggedIn || currentScreen is AppScreen.Login) {
    LoginScreen(
      onLoginSuccess = { u, p -> viewModel.login(u, p) },
      schoolName = viewModel.schoolName,
      modifier = modifier
    )
    return
  }

  // Responsive Layout
  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
  ) {
    val isTablet = maxWidth >= 720.dp

    if (isTablet) {
      // Tablet-First: Persistent Left Sidebar + Main Content Right Pane
      Row(modifier = Modifier.fillMaxSize()) {
        FeeTrackSidebar(
          currentScreen = currentScreen,
          onNavigate = { viewModel.navigateTo(it) },
          onLogout = { viewModel.logout() },
          bursarName = viewModel.adminName,
          bursarRole = viewModel.adminRole,
          schoolName = viewModel.schoolName,
          sessionName = viewModel.academicSession
        )

        // Right Pane
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) },
          topBar = {
            TabletTopBar(
              schoolName = viewModel.schoolName,
              session = viewModel.academicSession,
              adminName = viewModel.adminName,
              onQuickPay = { viewModel.navigateTo(AppScreen.RecordPayment()) }
            )
          },
          containerColor = BackgroundSlate,
          modifier = Modifier.weight(1f)
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            ScreenContent(
              currentScreen = currentScreen,
              viewModel = viewModel,
              dashboardStats = dashboardStats,
              recentPayments = recentPayments,
              allStudents = allStudents,
              filteredStudents = filteredStudents,
              studentSearch = studentSearch,
              studentClass = studentClass,
              studentStatus = studentStatus,
              selectedStudent = selectedStudent,
              selectedBreakdown = selectedBreakdown,
              selectedPayments = selectedPayments,
              filteredPayments = filteredPayments,
              paymentSearch = paymentSearch,
              paymentClass = paymentClass,
              paymentMethod = paymentMethod,
              paymentTerm = paymentTerm,
              recordStudentId = recordStudentId,
              recordAmount = recordAmount,
              recordMethod = recordMethod,
              recordCategory = recordCategory,
              recordTerm = recordTerm,
              recordDate = recordDate,
              recordReceiptNo = recordReceiptNo,
              recordReference = recordReference,
              paymentError = paymentError,
              feeClass = feeClass,
              feeTerm = feeTerm,
              currentClassStructures = currentClassStructures,
              feeSavedMsg = feeSavedMsg,
              classStats = classStats,
              paymentMethodStats = paymentMethodStats,
              leaderboardItems = leaderboardItems
            )
          }
        }
      }
    } else {
      // Mobile / Compact Layout: Drawer + TopAppBar
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet(drawerContainerColor = Color(0xFF042F2C)) {
            FeeTrackSidebar(
              currentScreen = currentScreen,
              onNavigate = { screen ->
                viewModel.navigateTo(screen)
                scope.launch { drawerState.close() }
              },
              onLogout = {
                viewModel.logout()
                scope.launch { drawerState.close() }
              },
              bursarName = viewModel.adminName,
              bursarRole = viewModel.adminRole,
              schoolName = viewModel.schoolName,
              sessionName = viewModel.academicSession
            )
          }
        }
      ) {
        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) },
          topBar = {
            TopAppBar(
              title = {
                Text(
                  text = when (currentScreen) {
                    AppScreen.Dashboard -> "Dashboard"
                    AppScreen.StudentsList -> "Students List"
                    is AppScreen.StudentProfile -> "Student Profile"
                    is AppScreen.RecordPayment -> "Record Payment"
                    AppScreen.FeeStructureSetup -> "Fee Structure"
                    AppScreen.PaymentHistory -> "Payment History"
                    AppScreen.Reports -> "Reports"
                    else -> "FeeTrack"
                  },
                  fontWeight = FontWeight.Bold,
                  color = PrimaryDarkTeal
                )
              },
              navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                  Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryDeepTeal)
                }
              },
              colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
          },
          containerColor = BackgroundSlate
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            ScreenContent(
              currentScreen = currentScreen,
              viewModel = viewModel,
              dashboardStats = dashboardStats,
              recentPayments = recentPayments,
              allStudents = allStudents,
              filteredStudents = filteredStudents,
              studentSearch = studentSearch,
              studentClass = studentClass,
              studentStatus = studentStatus,
              selectedStudent = selectedStudent,
              selectedBreakdown = selectedBreakdown,
              selectedPayments = selectedPayments,
              filteredPayments = filteredPayments,
              paymentSearch = paymentSearch,
              paymentClass = paymentClass,
              paymentMethod = paymentMethod,
              paymentTerm = paymentTerm,
              recordStudentId = recordStudentId,
              recordAmount = recordAmount,
              recordMethod = recordMethod,
              recordCategory = recordCategory,
              recordTerm = recordTerm,
              recordDate = recordDate,
              recordReceiptNo = recordReceiptNo,
              recordReference = recordReference,
              paymentError = paymentError,
              feeClass = feeClass,
              feeTerm = feeTerm,
              currentClassStructures = currentClassStructures,
              feeSavedMsg = feeSavedMsg,
              classStats = classStats,
              paymentMethodStats = paymentMethodStats,
              leaderboardItems = leaderboardItems
            )
          }
        }
      }
    }

    // Global Receipt Modal Dialog
    if (showReceiptModal && activeReceipt != null) {
      ReceiptDialog(
        payment = activeReceipt!!,
        onDismiss = { viewModel.closeReceiptModal() },
        onPrintOrShare = {
          scope.launch {
            snackbarHostState.showSnackbar("Receipt ${activeReceipt!!.receiptNumber} ready for printing & archiving.")
          }
        }
      )
    }

    // Global Export Dialog
    if (showExport) {
      ExportDialog(
        title = exportTitle,
        dataContent = exportData,
        onDismiss = { viewModel.showExportDialog.value = false }
      )
    }
  }
}

@Composable
private fun TabletTopBar(
  schoolName: String,
  session: String,
  adminName: String,
  onQuickPay: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp),
    color = Color.White,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = PrimaryTealContainer
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.School, contentDescription = null, tint = PrimaryDeepTeal, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = schoolName, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryDarkTeal)
          }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = session, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFFF1F5F9),
          modifier = Modifier.padding(end = 12.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF22C55E))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Bursary Gateway: Online", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
          }
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(PrimaryTealContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = PrimaryDarkTeal, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}

@Composable
private fun ScreenContent(
  currentScreen: AppScreen,
  viewModel: FeeTrackViewModel,
  dashboardStats: com.example.data.model.DashboardStats,
  recentPayments: List<com.example.data.model.PaymentEntity>,
  allStudents: List<com.example.data.model.StudentEntity>,
  filteredStudents: List<com.example.data.model.StudentEntity>,
  studentSearch: String,
  studentClass: String,
  studentStatus: String,
  selectedStudent: com.example.data.model.StudentEntity?,
  selectedBreakdown: List<com.example.data.model.FeeCategoryBreakdown>,
  selectedPayments: List<com.example.data.model.PaymentEntity>,
  filteredPayments: List<com.example.data.model.PaymentEntity>,
  paymentSearch: String,
  paymentClass: String,
  paymentMethod: String,
  paymentTerm: String,
  recordStudentId: Long?,
  recordAmount: String,
  recordMethod: String,
  recordCategory: String,
  recordTerm: String,
  recordDate: String,
  recordReceiptNo: String,
  recordReference: String,
  paymentError: String?,
  feeClass: String,
  feeTerm: String,
  currentClassStructures: List<com.example.data.model.FeeStructureEntity>,
  feeSavedMsg: String?,
  classStats: List<com.example.data.model.ClassCollectionStat>,
  paymentMethodStats: List<com.example.data.model.PaymentMethodStat>,
  leaderboardItems: List<com.example.data.model.LeaderboardItem>
) {
  when (currentScreen) {
    AppScreen.Dashboard -> {
      DashboardScreen(
        stats = dashboardStats,
        recentPayments = recentPayments,
        onNavigate = { viewModel.navigateTo(it) },
        onViewReceipt = { viewModel.viewReceipt(it) },
        onExportReport = { viewModel.exportReportsSummary() }
      )
    }

    AppScreen.StudentsList -> {
      StudentsListScreen(
        students = filteredStudents,
        searchQuery = studentSearch,
        onSearchChange = { viewModel.studentSearchQuery.value = it },
        selectedClass = studentClass,
        onClassFilterChange = { viewModel.studentClassFilter.value = it },
        selectedStatus = studentStatus,
        onStatusFilterChange = { viewModel.studentStatusFilter.value = it },
        onStudentClick = { viewModel.openStudentProfile(it) },
        onPayForStudent = { viewModel.preparePaymentForStudent(it) }
      )
    }

    is AppScreen.StudentProfile -> {
      StudentProfileScreen(
        student = selectedStudent,
        breakdown = selectedBreakdown,
        payments = selectedPayments,
        onBack = { viewModel.navigateBack() },
        onRecordPayment = { viewModel.preparePaymentForStudent(it) },
        onViewReceipt = { viewModel.viewReceipt(it) }
      )
    }

    is AppScreen.RecordPayment -> {
      RecordPaymentScreen(
        students = allStudents,
        selectedStudentId = recordStudentId,
        onStudentSelected = { viewModel.recordPaymentStudentId.value = it },
        amount = recordAmount,
        onAmountChange = { viewModel.recordPaymentAmount.value = it },
        paymentMethod = recordMethod,
        onPaymentMethodChange = { viewModel.recordPaymentMethod.value = it },
        category = recordCategory,
        onCategoryChange = { viewModel.recordPaymentCategory.value = it },
        term = recordTerm,
        onTermChange = { viewModel.recordPaymentTerm.value = it },
        date = recordDate,
        onDateChange = { viewModel.recordPaymentDate.value = it },
        receiptNumber = recordReceiptNo,
        onReceiptNumberChange = { viewModel.recordPaymentReceiptNo.value = it },
        referenceNote = recordReference,
        onReferenceChange = { viewModel.recordPaymentReference.value = it },
        errorMessage = paymentError,
        onSavePayment = { viewModel.savePayment() }
      )
    }

    AppScreen.FeeStructureSetup -> {
      FeeStructureScreen(
        selectedClass = feeClass,
        onClassChange = { viewModel.feeStructureClass.value = it },
        selectedTerm = feeTerm,
        onTermChange = { viewModel.feeStructureTerm.value = it },
        structures = currentClassStructures,
        onSaveStructure = { viewModel.saveFeeStructureChanges(it) },
        savedMessage = feeSavedMsg
      )
    }

    AppScreen.PaymentHistory -> {
      PaymentHistoryScreen(
        payments = filteredPayments,
        searchQuery = paymentSearch,
        onSearchChange = { viewModel.paymentHistorySearch.value = it },
        selectedClass = paymentClass,
        onClassChange = { viewModel.paymentHistoryClassFilter.value = it },
        selectedMethod = paymentMethod,
        onMethodChange = { viewModel.paymentHistoryMethodFilter.value = it },
        selectedTerm = paymentTerm,
        onTermChange = { viewModel.paymentHistoryTermFilter.value = it },
        onExport = { viewModel.exportPaymentHistory() },
        onViewReceipt = { viewModel.viewReceipt(it) }
      )
    }

    AppScreen.Reports -> {
      ReportsScreen(
        stats = dashboardStats,
        classStats = classStats,
        paymentMethodStats = paymentMethodStats,
        leaderboardItems = leaderboardItems,
        onExportSummary = { viewModel.exportReportsSummary() },
        onSendReminder = { viewModel.sendPaymentReminder(it) },
        onStudentClick = { viewModel.openStudentProfile(it) }
      )
    }

    AppScreen.Login -> {
      // Handled at top level
    }
  }
}
