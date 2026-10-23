package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassCollectionStat
import com.example.data.model.DashboardStats
import com.example.data.model.LeaderboardItem
import com.example.data.model.PaymentMethodStat
import com.example.ui.components.ClassCollectionsBarChart
import com.example.ui.components.PaymentMethodsPieChart
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PendingAmberContainer
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedDark
import java.util.Locale

@Composable
fun ReportsScreen(
  stats: DashboardStats,
  classStats: List<ClassCollectionStat>,
  paymentMethodStats: List<PaymentMethodStat>,
  leaderboardItems: List<LeaderboardItem>,
  onExportSummary: () -> Unit,
  onSendReminder: (LeaderboardItem) -> Unit,
  onStudentClick: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .testTag("reports_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Financial Analytics & Reports",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryDarkTeal
          )
          Text(
            text = "Comparative analytics, channel breakdown and arrears default tracking",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
          )
        }

        Button(
          onClick = onExportSummary,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
          modifier = Modifier.testTag("export_reports_summary_btn")
        ) {
          Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Export Executive Summary", fontWeight = FontWeight.Bold)
        }
      }
    }

    // Interactive Charts Row (Responsive BoxWithConstraints)
    item {
      BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isWide = maxWidth >= 840.dp
        if (isWide) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            ClassCollectionsBarChart(
              stats = classStats,
              modifier = Modifier.weight(1.5f)
            )
            PaymentMethodsPieChart(
              methodStats = paymentMethodStats,
              modifier = Modifier.weight(1f)
            )
          }
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ClassCollectionsBarChart(
              stats = classStats,
              modifier = Modifier.fillMaxWidth()
            )
            PaymentMethodsPieChart(
              methodStats = paymentMethodStats,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
    }

    // Outstanding Balance Leaderboard
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("outstanding_leaderboard_card"),
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(WarningRedContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Leaderboard, contentDescription = null, tint = WarningRedDark, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Outstanding Balance Leaderboard",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
                Text(
                  text = "Students with highest pending term arrears requiring follow-up notice",
                  style = MaterialTheme.typography.bodySmall,
                  color = TextSecondary
                )
              }
            }

            Surface(shape = RoundedCornerShape(8.dp), color = WarningRedContainer) {
              Text(
                text = "${leaderboardItems.size} Priority Accounts",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = WarningRedDark,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Leaderboard Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC))
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Rank", modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Student Name", modifier = Modifier.weight(2.0f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Class", modifier = Modifier.weight(1.0f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Outstanding Balance", modifier = Modifier.weight(1.4f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Days Overdue", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Action", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          }

          HorizontalDivider(color = Color(0xFFE2E8F0))

          leaderboardItems.forEach { item ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Rank badge
              Box(
                modifier = Modifier
                  .weight(0.7f)
              ) {
                Surface(
                  shape = CircleShape,
                  color = if (item.rank <= 3) WarningRedContainer else Color(0xFFF1F5F9)
                ) {
                  Text(
                    text = "#${item.rank}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (item.rank <= 3) WarningRedDark else TextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontSize = 10.sp
                  )
                }
              }

              // Student
              Column(modifier = Modifier.weight(2.0f)) {
                Text(
                  text = item.studentName,
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.SemiBold,
                  color = TextPrimary
                )
                Text(
                  text = item.studentCode,
                  style = MaterialTheme.typography.labelSmall,
                  fontFamily = FontFamily.Monospace,
                  color = TextMuted,
                  fontSize = 10.sp
                )
              }

              // Class
              Text(
                text = item.classLevel,
                modifier = Modifier.weight(1.0f),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )

              // Balance
              Text(
                text = "$${String.format(Locale.US, "%,.2f", item.balance)}",
                modifier = Modifier.weight(1.4f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.ExtraBold,
                color = WarningRedDark
              )

              // Days Overdue
              Text(
                text = "${item.daysOverdue} days",
                modifier = Modifier.weight(1.2f),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )

              // Action: Send Reminder Button
              OutlinedButton(
                onClick = { onSendReminder(item) },
                modifier = Modifier
                  .weight(1.2f)
                  .height(32.dp)
                  .testTag("send_reminder_btn_${item.studentCode}"),
                shape = RoundedCornerShape(6.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
              ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Remind", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }
            HorizontalDivider(color = Color(0xFFF1F5F9))
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
