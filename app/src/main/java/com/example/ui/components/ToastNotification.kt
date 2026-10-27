package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.viewmodel.ToastEvent
import kotlinx.coroutines.delay

@Composable
fun ToastNotification(
  toastEvent: ToastEvent?,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(toastEvent?.timestamp) {
    if (toastEvent != null) {
      delay(2800)
      onDismiss()
    }
  }

  AnimatedVisibility(
    visible = toastEvent != null,
    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
    modifier = modifier
  ) {
    if (toastEvent != null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .shadow(16.dp, RoundedCornerShape(12.dp))
          .clip(RoundedCornerShape(12.dp))
          .background(ReVideoSurface3)
          .border(1.dp, ReVideoBorder, RoundedCornerShape(12.dp))
          .padding(horizontal = 14.dp, vertical = 10.dp)
          .testTag("app_toast_notification")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = ReVideoCyan,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = toastEvent.message,
              color = ReVideoTextHigh,
              fontSize = 13.sp
            )
          }

          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dismiss",
            tint = ReVideoTextMedium,
            modifier = Modifier
              .size(16.dp)
              .clickable { onDismiss() }
          )
        }
      }
    }
  }
}
