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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FeeStructureEntity
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SuccessGreenDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun FeeStructureScreen(
  selectedClass: String,
  onClassChange: (String) -> Unit,
  selectedTerm: String,
  onTermChange: (String) -> Unit,
  structures: List<FeeStructureEntity>,
  onSaveStructure: (List<FeeStructureEntity>) -> Unit,
  savedMessage: String?,
  modifier: Modifier = Modifier
) {
  val classes = listOf("Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
  val terms = listOf("Term 1", "Term 2", "Term 3")

  // Local state copy for editing amounts
  val editableItems = remember(structures, selectedClass, selectedTerm) {
    val items = structures.ifEmpty {
      listOf(
        FeeStructureEntity(classLevel = selectedClass, term = selectedTerm, category = "Tuition Fee", amount = 1450.0),
        FeeStructureEntity(classLevel = selectedClass, term = selectedTerm, category = "Books & Stationery", amount = 250.0),
        FeeStructureEntity(classLevel = selectedClass, term = selectedTerm, category = "School Uniform & Kit", amount = 180.0),
        FeeStructureEntity(classLevel = selectedClass, term = selectedTerm, category = "Science & ICT Lab", amount = 150.0),
        FeeStructureEntity(classLevel = selectedClass, term = selectedTerm, category = "Sports & Activities", amount = 120.0)
      )
    }
    mutableStateListOf<FeeStructureEntity>().apply { addAll(items) }
  }

  var newCategoryName by remember { mutableStateOf("") }
  var newCategoryAmount by remember { mutableStateOf("") }
  var showAddCategoryRow by remember { mutableStateOf(false) }

  val totalFee = editableItems.sumOf { it.amount }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .padding(horizontal = 24.dp, vertical = 20.dp)
      .verticalScroll(rememberScrollState())
      .testTag("fee_structure_screen"),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Fee Structure Setup",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = PrimaryDarkTeal
        )
        Text(
          text = "Define and configure fee amounts per academic class and term",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary
        )
      }

      Button(
        onClick = { onSaveStructure(editableItems.toList()) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal),
        modifier = Modifier.testTag("save_fee_structure_btn")
      ) {
        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Save Structure", fontWeight = FontWeight.Bold)
      }
    }

    if (savedMessage != null) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = SuccessGreenContainer
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreenDark)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = savedMessage, style = MaterialTheme.typography.bodySmall, color = SuccessGreenDark, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Class & Term Selector Filters
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        // Class Selection
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Target Class:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          classes.forEach { cls ->
            val isSelected = selectedClass == cls
            FilterChip(
              selected = isSelected,
              onClick = { onClassChange(cls) },
              label = { Text(cls) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDeepTeal,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("fee_class_tab_$cls")
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Term Selection
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Term Session:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = TextSecondary)
          terms.forEach { t ->
            val isSelected = selectedTerm == t
            FilterChip(
              selected = isSelected,
              onClick = { onTermChange(t) },
              label = { Text(t) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryDarkTeal,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("fee_term_tab_$t")
            )
          }
        }
      }
    }

    // Editable Fee Structure Card
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
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "$selectedClass - $selectedTerm Fee Schedule",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
            Text(
              text = "Adjust item amounts directly in the input boxes below",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary
            )
          }

          // Total Badge
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = PrimaryTealContainer
          ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
              Text(text = "TOTAL FEE / STUDENT", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryDarkTeal, fontSize = 9.sp)
              Text(
                text = "$${String.format(Locale.US, "%,.2f", totalFee)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryDarkTeal
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0))
        Spacer(modifier = Modifier.height(12.dp))

        // Fee Items List
        editableItems.forEachIndexed { index, item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(2f)) {
              Text(
                text = item.category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
              )
              Text(
                text = if (item.isMandatory) "Mandatory Component" else "Optional Activity",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
              )
            }

            // Editable Amount
            OutlinedTextField(
              value = item.amount.toInt().toString(),
              onValueChange = { newVal ->
                val parsed = newVal.toDoubleOrNull() ?: 0.0
                editableItems[index] = item.copy(amount = parsed)
              },
              leadingIcon = {
                Text(text = "$", fontWeight = FontWeight.Bold, color = PrimaryDeepTeal, modifier = Modifier.padding(start = 8.dp))
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier
                .width(140.dp)
                .testTag("fee_input_${item.category.lowercase().replace(" ", "_")}"),
              shape = RoundedCornerShape(8.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryDeepTeal
              )
            )

            IconButton(
              onClick = {
                if (editableItems.size > 1) {
                  editableItems.removeAt(index)
                }
              },
              modifier = Modifier.padding(start = 8.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Delete item", tint = Color(0xFF94A3B8))
            }
          }
          HorizontalDivider(color = Color(0xFFF1F5F9))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Add Category Section
        if (showAddCategoryRow) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = newCategoryName,
              onValueChange = { newCategoryName = it },
              placeholder = { Text("Category Name (e.g. PTA Levy)") },
              modifier = Modifier.weight(2f),
              shape = RoundedCornerShape(8.dp)
            )
            OutlinedTextField(
              value = newCategoryAmount,
              onValueChange = { newCategoryAmount = it },
              placeholder = { Text("Amount ($)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.weight(1.2f),
              shape = RoundedCornerShape(8.dp)
            )
            Button(
              onClick = {
                if (newCategoryName.isNotBlank()) {
                  val amt = newCategoryAmount.toDoubleOrNull() ?: 50.0
                  editableItems.add(
                    FeeStructureEntity(
                      classLevel = selectedClass,
                      term = selectedTerm,
                      category = newCategoryName,
                      amount = amt
                    )
                  )
                  newCategoryName = ""
                  newCategoryAmount = ""
                  showAddCategoryRow = false
                }
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal)
            ) {
              Text("Add")
            }
          }
        } else {
          OutlinedButton(
            onClick = { showAddCategoryRow = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("add_fee_category_btn")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add New Fee Item")
          }
        }
      }
    }

    // Class Comparison Summary Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Fee Benchmark Across Grade Levels",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Comparative tuition and cumulative fees across senior and junior secondary levels",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary
        )
        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          ClassSummaryPill("Grade 7", "$1,900.00")
          ClassSummaryPill("Grade 8", "$1,900.00")
          ClassSummaryPill("Grade 9", "$2,150.00")
          ClassSummaryPill("Grade 10", "$2,150.00")
          ClassSummaryPill("Grade 11", "$2,400.00")
          ClassSummaryPill("Grade 12", "$2,400.00")
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun ClassSummaryPill(className: String, total: String) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFFF8FAFC)
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = className, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary)
      Text(text = total, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold, color = PrimaryDarkTeal)
    }
  }
}
