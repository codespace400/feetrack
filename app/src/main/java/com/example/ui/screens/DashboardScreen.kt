package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DashboardStats
import com.example.data.model.PaymentEntity
import com.example.ui.AppScreen
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.PendingAmberContainer
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SuccessGreenDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedDark
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
  stats: DashboardStats,
  recentPayments: List<PaymentEntity>,
  onNavigate: (AppScreen) -> Unit,
  onViewReceipt: (PaymentEntity) -> Unit,
  onExportReport: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .testTag("dashboard_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header Greeting & Quick Actions
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Bursary Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryDarkTeal
          )
          Text(
            text = "Welcome back, Sarah • Academic Year 2026/2027 (Term 2)",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onExportReport,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("dashboard_export_btn")
          ) {
            Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Export Report")
          }

          Button(
            onClick = { onNavigate(AppScreen.RecordPayment()) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
            modifier = Modifier.testTag("dashboard_record_pay_btn")
          ) {
            Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Record Payment")
          }
        }
      }
    }

    // 4 Stat Cards: Responsive layout
    item {
      BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isWide = maxWidth >= 760.dp
        if (isWide) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            StatCard(
              title = "Total Expected",
              value = "$${String.format(Locale.US, "%,.2f", stats.totalExpected)}",
              subtitle = "${stats.totalStudents} enrolled students",
              icon = Icons.Default.AccountBalance,
              iconBgColor = PrimaryTealContainer,
              iconTint = PrimaryDeepTeal,
              modifier = Modifier.weight(1f)
            )
            StatCard(
              title = "Total Collected",
              value = "$${String.format(Locale.US, "%,.2f", stats.totalCollected)}",
              subtitle = "Active cashflow",
              icon = Icons.Default.TrendingUp,
              iconBgColor = SuccessGreenContainer,
              iconTint = SuccessGreenDark,
              badgeText = "+14.8%",
              badgeColor = SuccessGreenContainer,
              badgeTextColor = SuccessGreenDark,
              modifier = Modifier.weight(1f)
            )
            StatCard(
              title = "Outstanding Balance",
              value = "$${String.format(Locale.US, "%,.2f", stats.outstandingBalance)}",
              subtitle = "Pending term fees",
              icon = Icons.Default.ErrorOutline,
              iconBgColor = WarningRedContainer,
              iconTint = WarningRedDark,
              badgeText = "Requires Follow-up",
              badgeColor = WarningRedContainer,
              badgeTextColor = WarningRedDark,
              modifier = Modifier.weight(1f)
            )
            StatCard(
              title = "Students in Default",
              value = "${stats.studentsInDefault}",
              subtitle = "Out of ${stats.totalStudents} students",
              icon = Icons.Default.People,
              iconBgColor = WarningRedContainer,
              iconTint = WarningRed,
              badgeText = "Owing List",
              badgeColor = WarningRedContainer,
              badgeTextColor = WarningRedDark,
              modifier = Modifier.weight(1f)
            )
          }
        } else {
          // 2x2 grid for narrower widths
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              StatCard(
                title = "Total Expected",
                value = "$${String.format(Locale.US, "%,.2f", stats.totalExpected)}",
                subtitle = "${stats.totalStudents} enrolled students",
                icon = Icons.Default.AccountBalance,
                iconBgColor = PrimaryTealContainer,
                iconTint = PrimaryDeepTeal,
                modifier = Modifier.weight(1f)
              )
              StatCard(
                title = "Total Collected",
                value = "$${String.format(Locale.US, "%,.2f", stats.totalCollected)}",
                subtitle = "Active cashflow",
                icon = Icons.Default.TrendingUp,
                iconBgColor = SuccessGreenContainer,
                iconTint = SuccessGreenDark,
                modifier = Modifier.weight(1f)
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              StatCard(
                title = "Outstanding Balance",
                value = "$${String.format(Locale.US, "%,.2f", stats.outstandingBalance)}",
                subtitle = "Pending fees",
                icon = Icons.Default.ErrorOutline,
                iconBgColor = WarningRedContainer,
                iconTint = WarningRedDark,
                modifier = Modifier.weight(1f)
              )
              StatCard(
                title = "Students in Default",
                value = "${stats.studentsInDefault}",
                subtitle = "Owing status",
                icon = Icons.Default.People,
                iconBgColor = WarningRedContainer,
                iconTint = WarningRed,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }

    // Collection Rate Progress Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("collection_rate_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Collection Rate Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Target collection benchmark: 90% before mid-term exams",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }

            Text(
              text = "${String.format(Locale.US, "%.1f", stats.collectionRate)}%",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = if (stats.collectionRate >= 70f) SuccessGreenDark else PendingAmber
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Progress Bar
          LinearProgressIndicator(
            progress = { (stats.collectionRate / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp)),
            color = SuccessGreen,
            trackColor = Color(0xFFE2E8F0)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Mini status breakdown badges
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(SuccessGreen)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Fully Cleared: ${(stats.totalStudents * 0.45).toInt()} students",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(PendingAmber)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Partial Payments: ${(stats.totalStudents * 0.35).toInt()} students",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(WarningRed)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Zero Payment: ${stats.studentsInDefault} students",
                style = MaterialTheme.typography.labelSmall,
                color = WarningRedDark,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    // Recent Payments Feed Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Recent Payments Feed",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Live transactions recorded across all school accounts",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }

        Row(
          modifier = Modifier
            .clickable { onNavigate(AppScreen.PaymentHistory) }
            .padding(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "View All History",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryDeepTeal
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = PrimaryDeepTeal,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }

    // Hybrid Table/Card for Recent Payments
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC))
              .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Receipt #",
              modifier = Modifier.weight(1.2f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Text(
              text = "Student Name",
              modifier = Modifier.weight(1.8f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Text(
              text = "Class",
              modifier = Modifier.weight(1f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Text(
              text = "Method",
              modifier = Modifier.weight(1.2f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Text(
              text = "Amount",
              modifier = Modifier.weight(1.2f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Text(
              text = "Action",
              modifier = Modifier.weight(0.8f),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
          }

          HorizontalDivider(color = Color(0xFFE2E8F0))

          if (recentPayments.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No payments recorded yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
              )
            }
          } else {
            recentPayments.forEach { payment ->
              RecentPaymentRow(
                payment = payment,
                onViewReceipt = { onViewReceipt(payment) }
              )
              HorizontalDivider(color = Color(0xFFF1F5F9))
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  iconBgColor: Color,
  iconTint: Color,
  badgeText: String? = null,
  badgeColor: Color = Color.Transparent,
  badgeTextColor: Color = Color.Transparent,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(iconBgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
          )
        }

        if (badgeText != null) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = badgeColor
          ) {
            Text(
              text = badgeText,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = badgeTextColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              fontSize = 10.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        fontWeight = FontWeight.Medium
      )

      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = TextPrimary
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        fontSize = 11.sp
      )
    }
  }
}

@Composable
fun RecentPaymentRow(
  payment: PaymentEntity,
  onViewReceipt: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = payment.receiptNumber,
      modifier = Modifier.weight(1.2f),
      style = MaterialTheme.typography.bodySmall,
      fontWeight = FontWeight.Bold,
      fontFamily = FontFamily.Monospace,
      color = PrimaryDarkTeal
    )

    Column(modifier = Modifier.weight(1.8f)) {
      Text(
        text = payment.studentName,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
      )
      Text(
        text = payment.paymentDate,
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        fontSize = 10.sp
      )
    }

    Text(
      text = payment.classLevel,
      modifier = Modifier.weight(1f),
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )

    // Method Badge
    Box(
      modifier = Modifier
        .weight(1.2f)
        .padding(end = 6.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFF1F5F9)
      ) {
        Text(
          text = payment.paymentMethod,
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
          fontSize = 10.sp
        )
      }
    }

    Text(
      text = "$${String.format(Locale.US, "%,.2f", payment.amount)}",
      modifier = Modifier.weight(1.2f),
      style = MaterialTheme.typography.bodySmall,
      fontWeight = FontWeight.Bold,
      color = SuccessGreenDark
    )

    IconButton(
      onClick = onViewReceipt,
      modifier = Modifier
        .weight(0.8f)
        .size(32.dp)
        .testTag("view_receipt_${payment.receiptNumber}")
    ) {
      Icon(
        imageVector = Icons.Default.Visibility,
        contentDescription = "View Receipt",
        tint = PrimaryDeepTeal,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}
