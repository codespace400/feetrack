package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassCollectionStat
import com.example.data.model.PaymentMethodStat
import com.example.ui.theme.PrimaryDeepTeal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ClassCollectionsBarChart(
  stats: List<ClassCollectionStat>,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.testTag("class_collections_bar_chart"),
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
            text = "Collections by Class",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = "Expected vs Collected fees across grade levels",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
        }

        // Legend
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(Color(0xFFCBD5E1))
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Expected", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
          Spacer(modifier = Modifier.width(12.dp))
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(SuccessGreen)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Collected", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Custom Canvas Bar Chart
      val maxExpected = stats.maxOfOrNull { it.expected }?.takeIf { it > 0 } ?: 3000.0

      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(220.dp)
      ) {
        val width = size.width
        val height = size.height
        val bottomPadding = 32.dp.toPx()
        val topPadding = 20.dp.toPx()
        val chartHeight = height - bottomPadding - topPadding

        // Horizontal Gridlines
        val gridSteps = 4
        for (i in 0..gridSteps) {
          val y = topPadding + (chartHeight * (i.toFloat() / gridSteps))
          drawLine(
            color = Color(0xFFF1F5F9),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1.dp.toPx()
          )
        }

        if (stats.isEmpty()) return@Canvas

        val barGroupWidth = width / stats.size
        val individualBarWidth = (barGroupWidth * 0.32f).coerceAtMost(32.dp.toPx())

        stats.forEachIndexed { index, stat ->
          val groupCenterX = (index * barGroupWidth) + (barGroupWidth / 2f)

          // Heights
          val expectedBarHeight = ((stat.expected / maxExpected) * chartHeight).toFloat()
          val collectedBarHeight = ((stat.collected / maxExpected) * chartHeight).toFloat()

          val baseY = height - bottomPadding

          // Draw Expected Bar (Left)
          val expectedX = groupCenterX - individualBarWidth - 2.dp.toPx()
          drawRoundRect(
            color = Color(0xFFE2E8F0),
            topLeft = Offset(expectedX, baseY - expectedBarHeight),
            size = Size(individualBarWidth, expectedBarHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
          )

          // Draw Collected Bar (Right)
          val collectedX = groupCenterX + 2.dp.toPx()
          drawRoundRect(
            color = SuccessGreen,
            topLeft = Offset(collectedX, baseY - collectedBarHeight),
            size = Size(individualBarWidth, collectedBarHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
          )

          // Draw Class Label
          drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
              color = android.graphics.Color.DKGRAY
              textSize = 10.sp.toPx()
              textAlign = android.graphics.Paint.Align.CENTER
              isAntiAlias = true
            }
            drawText(
              stat.classLevel.replace("Grade ", "Gr "),
              groupCenterX,
              height - 8.dp.toPx(),
              paint
            )
          }
        }
      }

      // Summary Pills
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        val avgRate = if (stats.isNotEmpty()) stats.map { it.collectionRate }.average() else 0.0
        val topClass = stats.maxByOrNull { it.collectionRate }
        Text(
          text = "Avg Collection: ${String.format(Locale.US, "%.1f", avgRate)}%",
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary
        )
        Text(
          text = "Top Performer: ${topClass?.classLevel ?: "N/A"} (${String.format(Locale.US, "%.0f", topClass?.collectionRate ?: 0f)}%)",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = PrimaryDeepTeal
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentMethodsPieChart(
  methodStats: List<PaymentMethodStat>,
  modifier: Modifier = Modifier
) {
  val colors = listOf(
    PrimaryDeepTeal,
    Color(0xFF0284C7), // Sky Blue
    Color(0xFFF59E0B), // Amber
    Color(0xFF8B5CF6)  // Purple
  )

  Card(
    modifier = modifier.testTag("payment_methods_pie_chart"),
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
        text = "Payment Channels",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
      Text(
        text = "Distribution across payment channels",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(160.dp)) {
          var startAngle = -90f
          val strokeWidth = 32.dp.toPx()
          val diameter = size.minDimension - strokeWidth
          val radius = diameter / 2f
          val center = Offset(size.width / 2f, size.height / 2f)

          if (methodStats.isEmpty()) {
            drawCircle(
              color = Color(0xFFE2E8F0),
              radius = radius,
              center = center,
              style = Stroke(width = strokeWidth)
            )
          } else {
            methodStats.forEachIndexed { index, stat ->
              val sweepAngle = (stat.percentage / 100f) * 360f
              val sliceColor = colors[index % colors.size]

              drawArc(
                color = sliceColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle.coerceAtLeast(2f),
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
              )
              startAngle += sweepAngle
            }
          }
        }

        // Center callout
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          val totalTxns = methodStats.sumOf { it.transactionCount }
          Text(
            text = "$totalTxns",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryDeepTeal
          )
          Text(
            text = "Total Txns",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Legend items
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        methodStats.forEachIndexed { index, stat ->
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colors[index % colors.size])
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${stat.method} (${String.format(Locale.US, "%.0f", stat.percentage)}%)",
              style = MaterialTheme.typography.labelSmall,
              color = TextPrimary
            )
          }
        }
      }
    }
  }
}
