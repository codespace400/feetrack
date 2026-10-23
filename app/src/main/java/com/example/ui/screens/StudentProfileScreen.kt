package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.FeeCategoryBreakdown
import com.example.data.model.PaymentEntity
import com.example.data.model.StudentEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SuccessGreenDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedDark
import java.util.Locale

@Composable
fun StudentProfileScreen(
  student: StudentEntity?,
  breakdown: List<FeeCategoryBreakdown>,
  payments: List<PaymentEntity>,
  onBack: () -> Unit,
  onRecordPayment: (Long) -> Unit,
  onViewReceipt: (PaymentEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  if (student == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(BackgroundSlate),
      contentAlignment = Alignment.Center
    ) {
      Text("Student profile not found", style = MaterialTheme.typography.bodyLarge)
    }
    return
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .testTag("student_profile_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Top Bar with Back
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack, modifier = Modifier.testTag("student_profile_back_btn")) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = PrimaryDeepTeal
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Student Fee Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryDarkTeal
          )
          Text(
            text = "Complete ledger breakdown and payment records",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }
      }
    }

    // Student Info Header Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            // Avatar & Name
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(PrimaryDeepTeal.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
              ) {
                val initials = student.name.split(" ")
                  .take(2)
                  .mapNotNull { it.firstOrNull()?.uppercase() }
                  .joinToString("")
                Text(
                  text = initials,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = PrimaryDarkTeal
                )
              }

              Spacer(modifier = Modifier.width(16.dp))

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  StatusBadge(status = student.status)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = student.studentCode,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDeepTeal
                  )
                  Text(
                    text = " • ${student.classLevel} • Academic Session 2026/2027",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                  )
                }
              }
            }

            // "Record Payment" Button
            Button(
              onClick = { onRecordPayment(student.id) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
              modifier = Modifier.testTag("profile_record_payment_btn")
            ) {
              Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Record Payment", fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(20.dp))
          HorizontalDivider(color = Color(0xFFF1F5F9))
          Spacer(modifier = Modifier.height(16.dp))

          // Guardian & Financial Details Grid
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            // Guardian details
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "GUARDIAN CONTACT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = student.guardianName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = student.guardianPhone, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              }
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
              ) {
                Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = student.guardianEmail, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              }
            }

            // Financial Summary Chips
            Row(
              modifier = Modifier.weight(1.4f),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              SummaryChip(
                label = "Total Expected",
                amount = "$${String.format(Locale.US, "%,.2f", student.totalFee)}",
                color = Color(0xFFF1F5F9),
                textColor = TextPrimary,
                modifier = Modifier.weight(1f)
              )
              SummaryChip(
                label = "Total Paid",
                amount = "$${String.format(Locale.US, "%,.2f", student.amountPaid)}",
                color = SuccessGreenContainer,
                textColor = SuccessGreenDark,
                modifier = Modifier.weight(1f)
              )
              SummaryChip(
                label = "Outstanding Balance",
                amount = "$${String.format(Locale.US, "%,.2f", student.balance)}",
                color = if (student.balance > 0) WarningRedContainer else Color(0xFFF1F5F9),
                textColor = if (student.balance > 0) WarningRedDark else TextSecondary,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }
    }

    // Fee Breakdown Table Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "Fee Breakdown by Term & Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Allocated fee items for ${student.classLevel} - ${student.term}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Breakdown Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC))
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Category", modifier = Modifier.weight(2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Term", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Billed Amount", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Amount Paid", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Balance", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Status", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          }

          HorizontalDivider(color = Color(0xFFE2E8F0))

          breakdown.forEach { item ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(item.category, modifier = Modifier.weight(2f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = TextPrimary)
              Text(item.term, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              Text("$${String.format(Locale.US, "%,.2f", item.billedAmount)}", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
              Text("$${String.format(Locale.US, "%,.2f", item.paidAmount)}", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = SuccessGreenDark)
              Text(
                "$${String.format(Locale.US, "%,.2f", item.balance)}",
                modifier = Modifier.weight(1.2f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (item.balance > 0) WarningRedDark else TextSecondary
              )
              Box(modifier = Modifier.weight(1f)) {
                val catStatus = if (item.balance <= 0) "PAID" else if (item.paidAmount > 0) "PARTIAL" else "OWING"
                StatusBadge(status = catStatus)
              }
            }
            HorizontalDivider(color = Color(0xFFF1F5F9))
          }
        }
      }
    }

    // Payment History Table Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "Student Payment History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Chronological record of fee settlements and verified receipts",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(14.dp))

          // History Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC))
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Receipt #", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Date", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Category", modifier = Modifier.weight(1.6f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Method", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Amount", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Recorded By", modifier = Modifier.weight(1.8f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("Receipt", modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          }

          HorizontalDivider(color = Color(0xFFE2E8F0))

          if (payments.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "No payments have been recorded for this student yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
              )
            }
          } else {
            payments.forEach { p ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = p.receiptNumber,
                  modifier = Modifier.weight(1.5f),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  color = PrimaryDarkTeal
                )
                Text(p.paymentDate, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(p.category, modifier = Modifier.weight(1.6f), style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                Text(p.paymentMethod, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(
                  text = "$${String.format(Locale.US, "%,.2f", p.amount)}",
                  modifier = Modifier.weight(1.2f),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = SuccessGreenDark
                )
                Text(p.recordedBy, modifier = Modifier.weight(1.8f), style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 11.sp)
                IconButton(
                  onClick = { onViewReceipt(p) },
                  modifier = Modifier.weight(0.8f).size(28.dp)
                ) {
                  Icon(Icons.Default.Visibility, contentDescription = "View Receipt", tint = PrimaryDeepTeal, modifier = Modifier.size(16.dp))
                }
              }
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
private fun SummaryChip(
  label: String,
  amount: String,
  color: Color,
  textColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(10.dp),
    color = color
  ) {
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
      Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = amount, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
  }
}
