package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.PaymentEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ReceiptDialog(
  payment: PaymentEntity,
  onDismiss: () -> Unit,
  onPrintOrShare: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .widthIn(max = 520.dp)
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("receipt_dialog"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(24.dp)
      ) {
        // Dialog Top Bar
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
                .background(SuccessGreenContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Payment Verified",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = "Official Bursary Receipt",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_receipt_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Receipt Card Document
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .background(BackgroundSlate),
          color = BackgroundSlate
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp)
          ) {
            // School Header
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = PrimaryDeepTeal,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "OAKRIDGE ACADEMY",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryDeepTeal,
                    letterSpacing = 1.sp
                  )
                }
                Text(
                  text = "School Fees Bursary Division",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextMuted
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = payment.receiptNumber,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  color = PrimaryDarkTeal
                )
                Text(
                  text = payment.paymentDate,
                  style = MaterialTheme.typography.labelSmall,
                  color = TextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Student Information Grid
            ReceiptRow(label = "Student Name", value = payment.studentName, isBold = true)
            ReceiptRow(label = "Student ID", value = payment.studentCode)
            ReceiptRow(label = "Class Level", value = payment.classLevel)
            ReceiptRow(label = "Academic Term", value = "${payment.term} (2026/2027)")
            ReceiptRow(label = "Fee Category", value = payment.category)
            ReceiptRow(label = "Payment Method", value = payment.paymentMethod)
            if (payment.referenceNote.isNotBlank()) {
              ReceiptRow(label = "Reference / Notes", value = payment.referenceNote)
            }
            ReceiptRow(label = "Received By", value = payment.recordedBy)

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFCBD5E1), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Total Amount Highlight Box
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryTealContainer)
                .padding(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "AMOUNT PAID",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDarkTeal
                  )
                  Text(
                    text = "Status: Cleared & Credited",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                  )
                }
                Text(
                  text = "$${String.format(Locale.US, "%,.2f", payment.amount)}",
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = PrimaryDarkTeal
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onPrintOrShare,
            modifier = Modifier
              .weight(1f)
              .testTag("print_receipt_btn")
          ) {
            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Print Receipt")
          }

          Button(
            onClick = onDismiss,
            modifier = Modifier
              .weight(1f)
              .testTag("done_receipt_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal)
          ) {
            Text("Done")
          }
        }
      }
    }
  }
}

@Composable
private fun ReceiptRow(label: String, value: String, isBold: Boolean = false) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodySmall,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
      color = TextPrimary
    )
  }
}
