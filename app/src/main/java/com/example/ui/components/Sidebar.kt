package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.theme.PrimaryTealLight
import com.example.ui.theme.SidebarBackground
import com.example.ui.theme.SidebarItemActive
import com.example.ui.theme.SidebarText
import com.example.ui.theme.SidebarTextMuted

data class NavItem(
  val label: String,
  val icon: ImageVector,
  val targetScreen: AppScreen,
  val badge: String? = null
)

@Composable
fun FeeTrackSidebar(
  currentScreen: AppScreen,
  onNavigate: (AppScreen) -> Unit,
  onLogout: () -> Unit,
  bursarName: String = "Mrs. Sarah Jenkins",
  bursarRole: String = "Chief Bursar",
  schoolName: String = "Oakridge Academy",
  sessionName: String = "2026/2027 • Term 2",
  modifier: Modifier = Modifier
) {
  val navItems = listOf(
    NavItem("Dashboard", Icons.Default.Dashboard, AppScreen.Dashboard),
    NavItem("Students List", Icons.Default.People, AppScreen.StudentsList),
    NavItem("Record Payment", Icons.Default.AddCard, AppScreen.RecordPayment()),
    NavItem("Fee Structure", Icons.Default.AccountBalance, AppScreen.FeeStructureSetup),
    NavItem("Payment History", Icons.Default.ReceiptLong, AppScreen.PaymentHistory),
    NavItem("Reports", Icons.Default.BarChart, AppScreen.Reports)
  )

  Surface(
    modifier = modifier
      .width(260.dp)
      .fillMaxHeight()
      .testTag("app_sidebar"),
    color = SidebarBackground,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        // App Header & Logo
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(PrimaryTealLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.School,
              contentDescription = null,
              tint = Color(0xFF042F2C),
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "FeeTrack",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Black,
              color = Color.White,
              letterSpacing = 0.5.sp
            )
            Text(
              text = schoolName,
              style = MaterialTheme.typography.labelSmall,
              color = SidebarTextMuted
            )
          }
        }

        // Active Session Badge
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F766E).copy(alpha = 0.35f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SESSION",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = PrimaryTealLight,
              fontSize = 9.sp
            )
            Text(
              text = sessionName,
              style = MaterialTheme.typography.labelSmall,
              color = SidebarText,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick "+ Record Payment" CTA
        Button(
          onClick = { onNavigate(AppScreen.RecordPayment()) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("sidebar_quick_pay_btn"),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryTealLight),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AddCard,
            contentDescription = null,
            tint = Color(0xFF042F2C),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Record Payment",
            color = Color(0xFF042F2C),
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
          text = "NAVIGATION",
          style = MaterialTheme.typography.labelSmall,
          color = SidebarTextMuted,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        // Nav Item list
        navItems.forEach { item ->
          val isSelected = when (item.targetScreen) {
            AppScreen.Dashboard -> currentScreen is AppScreen.Dashboard
            AppScreen.StudentsList -> currentScreen is AppScreen.StudentsList || currentScreen is AppScreen.StudentProfile
            is AppScreen.RecordPayment -> currentScreen is AppScreen.RecordPayment
            AppScreen.FeeStructureSetup -> currentScreen is AppScreen.FeeStructureSetup
            AppScreen.PaymentHistory -> currentScreen is AppScreen.PaymentHistory
            AppScreen.Reports -> currentScreen is AppScreen.Reports
            else -> false
          }

          SidebarNavigationRow(
            item = item,
            isSelected = isSelected,
            onClick = { onNavigate(item.targetScreen) }
          )
        }
      }

      // Bottom User & Logout section
      Column(
        modifier = Modifier.padding(top = 16.dp)
      ) {
        HorizontalDivider(color = Color(0xFF134E4A), thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Bursar Profile Card
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0F766E).copy(alpha = 0.2f))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(PrimaryTealLight),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "SJ",
              fontWeight = FontWeight.Bold,
              color = Color(0xFF042F2C),
              fontSize = 13.sp
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = bursarName,
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = Color.White
            )
            Text(
              text = bursarRole,
              style = MaterialTheme.typography.labelSmall,
              color = SidebarTextMuted,
              fontSize = 10.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Logout Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLogout() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .testTag("sidebar_logout_btn"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Log Out",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Log Out",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFFCA5A5)
          )
        }
      }
    }
  }
}

@Composable
private fun SidebarNavigationRow(
  item: NavItem,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val background = if (isSelected) SidebarItemActive else Color.Transparent
  val contentColor = if (isSelected) Color.White else SidebarText
  val iconTint = if (isSelected) PrimaryTealLight else SidebarTextMuted

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(background)
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 10.dp)
      .testTag("nav_item_${item.label.lowercase().replace(" ", "_")}"),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = item.icon,
      contentDescription = item.label,
      tint = iconTint,
      modifier = Modifier.size(20.dp)
    )

    Spacer(modifier = Modifier.width(12.dp))

    Text(
      text = item.label,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      color = contentColor,
      modifier = Modifier.weight(1f)
    )

    if (item.badge != null) {
      Box(
        modifier = Modifier
          .clip(CircleShape)
          .background(PrimaryTealLight)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(
          text = item.badge,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF042F2C),
          fontWeight = FontWeight.Bold,
          fontSize = 9.sp
        )
      }
    }
  }
}
