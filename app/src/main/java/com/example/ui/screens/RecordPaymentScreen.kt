package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.SuccessGreenDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import com.example.ui.theme.WarningRedContainer
import com.example.ui.theme.WarningRedDark
import java.util.Locale
import kotlin.random.Random

@Composable
fun RecordPaymentScreen(
  students: List<StudentEntity>,
  selectedStudentId: Long?,
  onStudentSelected: (Long) -> Unit,
  amount: String,
  onAmountChange: (String) -> Unit,
  paymentMethod: String,
  onPaymentMethodChange: (String) -> Unit,
  category: String,
  onCategoryChange: (String) -> Unit,
  term: String,
  onTermChange: (String) -> Unit,
  date: String,
  onDateChange: (String) -> Unit,
  receiptNumber: String,
  onReceiptNumberChange: (String) -> Unit,
  referenceNote: String,
  onReferenceChange: (String) -> Unit,
  errorMessage: String?,
  onSavePayment: () -> Unit,
  modifier: Modifier = Modifier
) {
  val selectedStudent = students.find { it.id == selectedStudentId }
  var studentDropdownExpanded by remember { mutableStateOf(false) }

  val paymentMethods = listOf("Bank Transfer", "Card / POS", "Cash", "Cheque")
  val categories = listOf("Tuition Fee", "Books & Stationery", "School Uniform & Kit", "Science & ICT Lab", "Sports & Activities")
  val terms = listOf("Term 1", "Term 2", "Term 3")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .verticalScroll(rememberScrollState())
      .testTag("record_payment_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header
    Column {
      Text(
        text = "Record Fee Payment",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.ExtraBold,
        color = PrimaryDarkTeal
      )
      Text(
        text = "Process and verify student fees, credit account ledger and issue receipt",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
      )
    }

    if (errorMessage != null) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = WarningRedContainer
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = WarningRedDark)
          Spacer(modifier = Modifier.width(10.dp))
          Text(text = errorMessage, style = MaterialTheme.typography.bodySmall, color = WarningRedDark, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // Main Payment Form Card
    Card(
      modifier = Modifier
        .widthIn(max = 840.dp)
        .fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        Text(
          text = "Payment Information",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )

        // 1. Student Selection Field
        Column {
          Text(
            text = "Select Student *",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(6.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = if (selectedStudent != null) {
                "${selectedStudent.name} (${selectedStudent.studentCode}) • ${selectedStudent.classLevel}"
              } else {
                ""
              },
              onValueChange = {},
              readOnly = true,
              placeholder = { Text("Click to select student from directory...") },
              leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryDeepTeal)
              },
              trailingIcon = {
                IconButton(onClick = { studentDropdownExpanded = true }) {
                  Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .clickable { studentDropdownExpanded = true }
                .testTag("select_student_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDeepTeal,
                unfocusedBorderColor = Color(0xFFCBD5E1)
              ),
              shape = RoundedCornerShape(10.dp)
            )

            DropdownMenu(
              expanded = studentDropdownExpanded,
              onDismissRequest = { studentDropdownExpanded = false },
              modifier = Modifier
                .width(420.dp)
                .background(Color.White)
            ) {
              students.forEach { s ->
                DropdownMenuItem(
                  text = {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Column {
                        Text(s.name, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text("${s.studentCode} • ${s.classLevel}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                      }
                      Column(horizontalAlignment = Alignment.End) {
                        Text(
                          text = "Bal: $${String.format(Locale.US, "%,.0f", s.balance)}",
                          style = MaterialTheme.typography.labelSmall,
                          fontWeight = FontWeight.Bold,
                          color = if (s.balance > 0) WarningRedDark else SuccessGreenDark
                        )
                        StatusBadge(status = s.status)
                      }
                    }
                  },
                  onClick = {
                    onStudentSelected(s.id)
                    studentDropdownExpanded = false
                  }
                )
                HorizontalDivider(color = Color(0xFFF1F5F9))
              }
            }
          }

          // Balance Context Banner
          if (selectedStudent != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp),
              color = if (selectedStudent.balance > 0) WarningRedContainer.copy(alpha = 0.6f) else PrimaryTealContainer
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Guardian: ${selectedStudent.guardianName} (${selectedStudent.guardianPhone})",
                  style = MaterialTheme.typography.labelSmall,
                  color = TextSecondary
                )
                Text(
                  text = "Outstanding Balance: $${String.format(Locale.US, "%,.2f", selectedStudent.balance)}",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = if (selectedStudent.balance > 0) WarningRedDark else PrimaryDarkTeal
                )
              }
            }
          }
        }

        // 2. Amount Input & Quick Chips
        Column {
          Text(
            text = "Payment Amount ($) *",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(6.dp))

          OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            placeholder = { Text("e.g. 500.00") },
            leadingIcon = {
              Text(
                text = "$",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDeepTeal,
                modifier = Modifier.padding(start = 12.dp)
              )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("payment_amount_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryDeepTeal,
              unfocusedBorderColor = Color(0xFFCBD5E1)
            ),
            shape = RoundedCornerShape(10.dp)
          )

          // Quick Chips for Student Balance
          if (selectedStudent != null && selectedStudent.balance > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = "Quick Fill:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
              QuickAmountChip(
                label = "Full Balance ($${selectedStudent.balance.toInt()})",
                onClick = { onAmountChange(selectedStudent.balance.toInt().toString()) }
              )
              val half = (selectedStudent.balance / 2).toInt()
              if (half > 0) {
                QuickAmountChip(
                  label = "50% ($$half)",
                  onClick = { onAmountChange(half.toString()) }
                )
              }
              QuickAmountChip(
                label = "$200",
                onClick = { onAmountChange("200") }
              )
            }
          }
        }

        // 3. Payment Method & Fee Category Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Payment Method Selector
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Payment Method *",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            var methodExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
              OutlinedTextField(
                value = paymentMethod,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                  IconButton(onClick = { methodExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { methodExpanded = true }
                  .testTag("select_method_input"),
                shape = RoundedCornerShape(10.dp)
              )

              DropdownMenu(
                expanded = methodExpanded,
                onDismissRequest = { methodExpanded = false }
              ) {
                paymentMethods.forEach { m ->
                  DropdownMenuItem(
                    text = { Text(m) },
                    onClick = {
                      onPaymentMethodChange(m)
                      methodExpanded = false
                    }
                  )
                }
              }
            }
          }

          // Category Allocation Selector
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Fee Category *",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            var catExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
              OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                  IconButton(onClick = { catExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { catExpanded = true }
                  .testTag("select_category_input"),
                shape = RoundedCornerShape(10.dp)
              )

              DropdownMenu(
                expanded = catExpanded,
                onDismissRequest = { catExpanded = false }
              ) {
                categories.forEach { c ->
                  DropdownMenuItem(
                    text = { Text(c) },
                    onClick = {
                      onCategoryChange(c)
                      catExpanded = false
                    }
                  )
                }
              }
            }
          }
        }

        // 4. Term, Date & Auto-Receipt Number Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Term
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "Academic Term", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            var termExpanded by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
              OutlinedTextField(
                value = term,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                  IconButton(onClick = { termExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { termExpanded = true },
                shape = RoundedCornerShape(10.dp)
              )
              DropdownMenu(
                expanded = termExpanded,
                onDismissRequest = { termExpanded = false }
              ) {
                terms.forEach { t ->
                  DropdownMenuItem(text = { Text(t) }, onClick = { onTermChange(t); termExpanded = false })
                }
              }
            }
          }

          // Date
          Column(modifier = Modifier.weight(1f)) {
            Text(text = "Payment Date", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
              value = date,
              onValueChange = onDateChange,
              trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp)
            )
          }

          // Auto-Generated Receipt Number
          Column(modifier = Modifier.weight(1.2f)) {
            Text(text = "Auto Receipt #", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
              value = receiptNumber,
              onValueChange = onReceiptNumberChange,
              trailingIcon = {
                IconButton(onClick = { onReceiptNumberChange("REC-2026-${Random.nextInt(8100, 9999)}") }) {
                  Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = PrimaryDeepTeal, modifier = Modifier.size(18.dp))
                }
              },
              textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = PrimaryDarkTeal
              ),
              modifier = Modifier.fillMaxWidth().testTag("receipt_number_input"),
              shape = RoundedCornerShape(10.dp)
            )
          }
        }

        // 5. Reference / Transaction Note
        Column {
          Text(
            text = "Reference / Transaction Note (Optional)",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(6.dp))
          OutlinedTextField(
            value = referenceNote,
            onValueChange = onReferenceChange,
            placeholder = { Text("e.g. Bank Wire Ref #99201, POS Auth #4829, or Cashier slip") },
            modifier = Modifier.fillMaxWidth().testTag("payment_reference_input"),
            shape = RoundedCornerShape(10.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Save Button
        Button(
          onClick = onSavePayment,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("save_payment_btn"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal)
        ) {
          Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Save Payment & Generate Receipt",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
private fun QuickAmountChip(
  label: String,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = Color(0xFFF1F5F9),
    modifier = Modifier.clickable { onClick() }
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.SemiBold,
      color = PrimaryDarkTeal,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}
