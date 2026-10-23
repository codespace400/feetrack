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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundSlate
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.PrimaryTealContainer
import com.example.ui.theme.PrimaryTealLight
import com.example.ui.theme.SidebarBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LoginScreen(
  onLoginSuccess: (String, String) -> Unit,
  schoolName: String = "Oakridge Academy",
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("bursar@feetrack.edu") }
  var password by remember { mutableStateOf("admin2026") }
  var passwordVisible by remember { mutableStateOf(false) }
  var selectedSession by remember { mutableStateOf("2026/2027 • Term 2") }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundSlate)
      .testTag("login_screen"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 480.dp)
        .fillMaxWidth()
        .padding(24.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Brand Crest
      Box(
        modifier = Modifier
          .size(68.dp)
          .clip(CircleShape)
          .background(PrimaryDeepTeal),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.School,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(38.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "FeeTrack",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        color = PrimaryDarkTeal
      )
      Text(
        text = "School Fees Management System",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
      )
      Text(
        text = schoolName,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = PrimaryDeepTeal
      )

      Spacer(modifier = Modifier.height(28.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp)
        ) {
          Text(
            text = "Administrative Access",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Sign in to access bursary accounts, fee ledgers & reports",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Email / Username
          OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Bursar Email / Username") },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryDeepTeal)
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_username_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryDeepTeal,
              focusedLabelColor = PrimaryDeepTeal
            )
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Password
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Security Password") },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryDeepTeal)
            },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                  imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = if (passwordVisible) "Hide password" else "Show password",
                  tint = TextMuted
                )
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Password,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = { onLoginSuccess(email, password) }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_password_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryDeepTeal,
              focusedLabelColor = PrimaryDeepTeal
            )
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Session Indicator Banner
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = PrimaryTealContainer.copy(alpha = 0.5f)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = PrimaryDeepTeal,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Active Session: $selectedSession",
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryDarkTeal,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          // Sign In Button
          Button(
            onClick = { onLoginSuccess(email, password) },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("login_submit_btn"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDeepTeal)
          ) {
            Text(
              text = "Sign In to FeeTrack",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Quick Demo Bursar Login Button
          OutlinedButton(
            onClick = {
              email = "sarah.jenkins@feetrack.edu"
              password = "bursar_master_2026"
              onLoginSuccess(email, password)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("login_demo_btn"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = "Quick Demo: Sign In as Chief Bursar",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = PrimaryDarkTeal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "Authorized Personnel Only • Encrypted Bursary Access",
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted
      )
    }
  }
}
