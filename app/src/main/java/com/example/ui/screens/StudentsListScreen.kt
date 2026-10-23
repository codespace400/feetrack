package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.OnPendingAmberContainer
import com.example.ui.theme.OnSuccessGreenContainer
import com.example.ui.theme.OnWarningRedContainer
import com.example.ui.theme.PendingAmberContainer
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
fun StudentsListScreen(
  students: List<StudentEntity>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedClass: String,
  onClassFilterChange: (String) -> Unit,
  selectedStatus: String,
  onStatusFilterChange: (String) -> Unit,
  onStudentClick: (Long) -> Unit,
  onPayForStudent: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val classes = listOf("All", "Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
  val statuses = listOf("All", "PAID", "PARTIAL", "OWING")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .testTag("students_list_screen")
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Students Fee Registry",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = PrimaryDarkTeal
        )
        Text(
          text = "Showing ${students.size} students across academic levels",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Search & Filter Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        // Search Input
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchChange,
          placeholder = { Text("Search by student name, ID or guardian...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryDeepTeal)
          },
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
            .testTag("student_search_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryDeepTeal,
            unfocusedBorderColor = Color(0xFFCBD5E1)
          ),
          shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Class Filter Chips Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Class:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )

          classes.forEach { cls ->
            val isSelected = selectedClass == cls
            FilterChip(
              selected = isSelected,
              onClick = { onClassFilterChange(cls) },
              label = { Text(cls) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDeepTeal,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("filter_class_$cls")
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status Filter Chips Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Status:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )

          statuses.forEach { stat ->
            val isSelected = selectedStatus == stat
            FilterChip(
              selected = isSelected,
              onClick = { onStatusFilterChange(stat) },
              label = {
                Text(
                  when (stat) {
                    "All" -> "All Statuses"
                    "PAID" -> "Paid"
                    "PARTIAL" -> "Partial"
                    "OWING" -> "Owing"
                    else -> stat
                  }
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = when (stat) {
                  "PAID" -> Color(0xFF15803D)
                  "PARTIAL" -> Color(0xFFD97706)
                  "OWING" -> Color(0xFFDC2626)
                  else -> PrimaryDeepTeal
                },
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("filter_status_$stat")
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Students Hybrid Table Card
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
          Text(
            text = "Student Info",
            modifier = Modifier.weight(2.2f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Class",
            modifier = Modifier.weight(1.0f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Total Fee",
            modifier = Modifier.weight(1.1f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Paid",
            modifier = Modifier.weight(1.1f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Balance",
            modifier = Modifier.weight(1.1f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Status",
            modifier = Modifier.weight(1.1f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
          Text(
            text = "Action",
            modifier = Modifier.weight(1.2f),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = TextSecondary
          )
        }

        HorizontalDivider(color = Color(0xFFE2E8F0))

        if (students.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "No students match the current filters.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
              )
            }
          }
        } else {
          LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(students, key = { it.id }) { student ->
              StudentTableRow(
                student = student,
                onClick = { onStudentClick(student.id) },
                onPay = { onPayForStudent(student.id) }
              )
              HorizontalDivider(color = Color(0xFFF1F5F9))
            }
          }
        }
      }
    }
  }
}

@Composable
private fun StudentTableRow(
  student: StudentEntity,
  onClick: () -> Unit,
  onPay: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("student_row_${student.id}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Student Info (Avatar + Name + Code)
    Row(
      modifier = Modifier.weight(2.2f),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
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
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = PrimaryDarkTeal
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Text(
          text = student.name,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = TextPrimary
        )
        Text(
          text = student.studentCode,
          style = MaterialTheme.typography.labelSmall,
          fontFamily = FontFamily.Monospace,
          color = TextMuted,
          fontSize = 10.sp
        )
      }
    }

    // Class
    Text(
      text = student.classLevel,
      modifier = Modifier.weight(1.0f),
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )

    // Total Fee
    Text(
      text = "$${String.format(Locale.US, "%,.0f", student.totalFee)}",
      modifier = Modifier.weight(1.1f),
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )

    // Amount Paid
    Text(
      text = "$${String.format(Locale.US, "%,.0f", student.amountPaid)}",
      modifier = Modifier.weight(1.1f),
      style = MaterialTheme.typography.bodySmall,
      fontWeight = FontWeight.SemiBold,
      color = SuccessGreenDark
    )

    // Balance
    Text(
      text = "$${String.format(Locale.US, "%,.0f", student.balance)}",
      modifier = Modifier.weight(1.1f),
      style = MaterialTheme.typography.bodySmall,
      fontWeight = FontWeight.Bold,
      color = if (student.balance > 0) WarningRedDark else TextSecondary
    )

    // Status Badge
    Box(modifier = Modifier.weight(1.1f)) {
      StatusBadge(status = student.status)
    }

    // Actions
    Row(
      modifier = Modifier.weight(1.2f),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (student.balance > 0) {
        Button(
          onClick = onPay,
          modifier = Modifier.height(30.dp),
          shape = RoundedCornerShape(6.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Pay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      IconButton(
        onClick = onClick,
        modifier = Modifier.size(30.dp)
      ) {
        Icon(
          imageVector = Icons.Default.ArrowForward,
          contentDescription = "View Profile",
          tint = PrimaryDeepTeal,
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}

@Composable
fun StatusBadge(status: String) {
  val (bgColor, textColor, label) = when (status.uppercase()) {
    "PAID" -> Triple(SuccessGreenContainer, OnSuccessGreenContainer, "Paid")
    "PARTIAL" -> Triple(PendingAmberContainer, OnPendingAmberContainer, "Partial")
    else -> Triple(WarningRedContainer, OnWarningRedContainer, "Owing")
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Bold,
      color = textColor,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
      fontSize = 11.sp
    )
  }
}
