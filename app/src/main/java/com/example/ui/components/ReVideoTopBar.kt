package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCanvas
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryDim
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium

@Composable
fun ReVideoTopBar(
  title: String = "ReVideo AI",
  subtitle: String = "Neural Video Studio",
  credits: Int = 45,
  showBackButton: Boolean = false,
  onBackClick: () -> Unit = {},
  onCreditsClick: () -> Unit = {},
  onNotificationsClick: () -> Unit = {},
  onProfileClick: () -> Unit = {},
  trailingActions: @Composable (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(ReVideoCanvas.copy(alpha = 0.96f))
      .border(1.dp, ReVideoBorderSubtle)
  ) {
    // Header content row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left side: Logo or Back + Title & Subtitle
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f, fill = false)
      ) {
        if (showBackButton) {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier
              .size(40.dp)
              .testTag("top_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = ReVideoTextHigh,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(4.dp))
        } else {
          // ReVideo stylized play icon
          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.linearGradient(
                  listOf(Color(0xFF232734), Color(0xFF171A24))
                )
              )
              .border(1.dp, ReVideoBorder, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(ReVideoCyan)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
        }

        Column(modifier = Modifier.padding(end = 8.dp)) {
          Text(
            text = title,
            color = ReVideoTextHigh,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = subtitle,
            color = ReVideoTextMedium,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      // Right side actions
      if (trailingActions != null) {
        trailingActions()
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Credits Badge Pill
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(ReVideoSurface1)
              .border(1.dp, ReVideoBorder, RoundedCornerShape(9999.dp))
              .clickable { onCreditsClick() }
              .padding(horizontal = 10.dp, vertical = 5.dp)
              .testTag("credits_badge"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = "Credits",
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "$credits credits",
              color = ReVideoPrimaryDim,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          // Notification Bell
          IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(ReVideoSurface1)
              .border(1.dp, ReVideoBorderSubtle, CircleShape)
              .testTag("notifications_button")
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Notifications",
              tint = ReVideoTextMedium,
              modifier = Modifier.size(18.dp)
            )
          }

          // Alex Avatar
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(ReVideoPrimary)
              .clickable { onProfileClick() }
              .testTag("profile_avatar"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "A",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
