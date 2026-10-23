package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.SuccessGreenDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun PaymentHistoryScreen(
  payments: List<PaymentEntity>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedClass: String,
  onClassChange: (String) -> Unit,
  selectedMethod: String,
  onMethodChange: (String) -> Unit,
  selectedTerm: String,
  onTermChange: (String) -> Unit,
  onExport: () -> Unit,
  onViewReceipt: (PaymentEntity) -> Unit,
  modifier: Modifier = Modifier
) {
  val classes = listOf("All", "Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
  val methods = listOf("All", "Bank Transfer", "Card / POS", "Cash", "Cheque")
  val terms = listOf("All", "Term 1", "Term 2", "Term 3")

  val totalFilteredAmount = payments.sumOf { it.amount }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .testTag("payment_history_screen")
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Fee Payment Audit Log",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = PrimaryDarkTeal
        )
        Text(
          text = "Complete verified transaction history and bursary clearing log",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
      }

      Button(
        onClick = onExport,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
        modifier = Modifier.testTag("export_payment_history_btn")
      ) {
        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Export CSV Records", fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Filter Controls Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChange,
          placeholder = { Text("Filter by student name, receipt #, or reference...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryDeepTeal) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchChange("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("payment_history_search_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryDeepTeal,
            unfocusedBorderColor = Color(0xFFCBD5E1)
          ),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Class Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Class:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary)
          classes.forEach { cls ->
            FilterChip(
              selected = selectedClass == cls,
              onClick = { onClassChange(cls) },
              label = { Text(cls, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDeepTeal,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Method Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Method:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary)
          methods.forEach { m ->
            FilterChip(
              selected = selectedMethod == m,
              onClick = { onMethodChange(m) },
              label = { Text(m, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDarkTeal,
                selectedLabelColor = Color.White
              )
            )
          }

          Spacer(modifier = Modifier.width(12.dp))
          Text("Term:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary)
          terms.forEach { t ->
            FilterChip(
              selected = selectedTerm == t,
              onClick = { onTermChange(t) },
              label = { Text(t, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDeepTeal,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Filter Summary Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${payments.size} transactions found",
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary
      )
      Text(
        text = "Total Filtered Value: $${String.format(Locale.US, "%,.2f", totalFilteredAmount)}",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = PrimaryDarkTeal
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Transactions Table Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Table Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Receipt #", modifier = Modifier.weight(1.3f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Date", modifier = Modifier.weight(1.0f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Student", modifier = Modifier.weight(1.8f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Class", modifier = Modifier.weight(0.9f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Category", modifier = Modifier.weight(1.4f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Method", modifier = Modifier.weight(1.1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Amount", modifier = Modifier.weight(1.1f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Recorded By", modifier = Modifier.weight(1.4f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          Text("Receipt", modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
        }

        HorizontalDivider(color = Color(0xFFE2E8F0))

        if (payments.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
              Spacer(modifier = Modifier.height(10.dp))
              Text("No transactions match the selected criteria.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
          }
        } else {
          LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(payments, key = { it.id }) { p ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = p.receiptNumber,
                  modifier = Modifier.weight(1.3f),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  color = PrimaryDarkTeal
                )
                Text(p.paymentDate, modifier = Modifier.weight(1.0f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                Column(modifier = Modifier.weight(1.8f)) {
                  Text(p.studentName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                  Text(p.studentCode, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, color = TextMuted, fontSize = 10.sp)
                }

                Text(p.classLevel, modifier = Modifier.weight(0.9f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(p.category, modifier = Modifier.weight(1.4f), style = MaterialTheme.typography.bodySmall, color = TextPrimary)

                Box(modifier = Modifier.weight(1.1f)) {
                  Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF1F5F9)) {
                    Text(
                      text = p.paymentMethod,
                      style = MaterialTheme.typography.labelSmall,
                      color = TextSecondary,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                      fontSize = 10.sp
                    )
                  }
                }

                Text(
                  text = "$${String.format(Locale.US, "%,.2f", p.amount)}",
                  modifier = Modifier.weight(1.1f),
                  style = MaterialTheme.typography.bodySmall,
                  fontWeight = FontWeight.Bold,
                  color = SuccessGreenDark
                )

                Text(p.recordedBy, modifier = Modifier.weight(1.4f), style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 11.sp)

                IconButton(
                  onClick = { onViewReceipt(p) },
                  modifier = Modifier.weight(0.8f).size(28.dp).testTag("view_receipt_btn_${p.receiptNumber}")
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
  }
}
