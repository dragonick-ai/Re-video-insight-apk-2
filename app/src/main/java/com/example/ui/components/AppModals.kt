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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ReferenceClip
import com.example.ui.theme.ReVideoBorder
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCyan
import com.example.ui.theme.ReVideoEmerald
import com.example.ui.theme.ReVideoPrimary
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoSurface1
import com.example.ui.theme.ReVideoSurface2
import com.example.ui.theme.ReVideoSurface3
import com.example.ui.theme.ReVideoTextHigh
import com.example.ui.theme.ReVideoTextMedium
import com.example.ui.theme.ReVideoTextSubtle

@Composable
fun CreditsDialog(
  credits: Int,
  onDismiss: () -> Unit,
  onTopUp: (Int) -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = ReVideoSurface2,
      border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
      modifier = Modifier.fillMaxWidth().testTag("credits_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = ReVideoPrimaryLight,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "Neural Credits Balance",
              color = ReVideoTextHigh,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = ReVideoTextMedium)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ReVideoSurface1)
            .border(1.dp, ReVideoBorderSubtle, RoundedCornerShape(12.dp))
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "$credits Credits",
              color = ReVideoPrimaryLight,
              fontSize = 28.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Each video generation consumes 10 credits",
              color = ReVideoTextMedium,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Quick Top-Up Options",
          color = ReVideoTextHigh,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        listOf(25, 50, 100).forEach { amount ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(ReVideoSurface3)
              .clickable {
                onTopUp(amount)
                onDismiss()
              }
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "+$amount Credits",
              color = ReVideoTextHigh,
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "Add to Balance",
              color = ReVideoCyan,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}

@Composable
fun NotificationsDialog(
  onDismiss: () -> Unit
) {
  val notifications = listOf(
    "Gemini 4 Argon video understanding model activated for scene decomposition.",
    "Gemini Omni 1.1 Flash Interactions API connected with 16:9 & 9:16 support.",
    "Runway Gen-3 Alpha V2V / Sora Engine motion vectors fully matched.",
    "Pro Plan active with 120 Video Mins remaining."
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = ReVideoSurface2,
      border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
      modifier = Modifier.fillMaxWidth().testTag("notifications_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = ReVideoPrimaryLight)
            Text(
              text = "Studio Notifications",
              color = ReVideoTextHigh,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = ReVideoTextMedium)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        notifications.forEach { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(ReVideoSurface1)
              .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(ReVideoEmerald)
                .padding(top = 4.dp)
            )
            Text(
              text = item,
              color = ReVideoTextHigh,
              fontSize = 12.sp,
              lineHeight = 16.sp
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProfileDialog(
  credits: Int,
  videoMinsLeft: Int,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = ReVideoSurface2,
      border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
      modifier = Modifier.fillMaxWidth().testTag("profile_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = ReVideoTextMedium)
          }
        }

        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(ReVideoPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "A", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Alex", color = ReVideoTextHigh, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = "dragonickplayer@gmail.com", color = ReVideoTextMedium, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(ReVideoSurface1)
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ReVideoEmerald))
          Text(text = "Pro Active", color = ReVideoTextMedium, fontSize = 11.sp, fontWeight = FontWeight.Medium)
          Text(text = "•", color = ReVideoTextSubtle, fontSize = 11.sp)
          Text(text = "$videoMinsLeft Video Mins Left", color = ReVideoTextHigh, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoPrimary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "Close Profile", color = Color.White)
        }
      }
    }
  }
}

@Composable
fun VideoPickerDialog(
  sampleClips: List<ReferenceClip>,
  selectedClip: ReferenceClip?,
  onSelectClip: (ReferenceClip) -> Unit,
  onLaunchDevicePicker: () -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = ReVideoSurface2,
      border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
      modifier = Modifier.fillMaxWidth().testTag("video_picker_dialog")
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Movie, contentDescription = null, tint = ReVideoCyan)
            Text(
              text = "Select Reference Video",
              color = ReVideoTextHigh,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = ReVideoTextMedium)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Device media picker button
        Button(
          onClick = {
            onLaunchDevicePicker()
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = ReVideoSurface3),
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ReVideoBorder),
          modifier = Modifier.fillMaxWidth().testTag("device_media_browse_btn")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.UploadFile, contentDescription = null, tint = ReVideoCyan)
            Text("Browse Device Files (MP4/MOV)", color = ReVideoTextHigh, fontSize = 13.sp)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Or Choose Studio Reference Clip",
          color = ReVideoTextMedium,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(sampleClips) { clip ->
            val isSelected = selectedClip?.id == clip.id
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) ReVideoSurface3 else ReVideoSurface1)
                .border(
                  1.dp,
                  if (isSelected) ReVideoPrimary else ReVideoBorderSubtle,
                  RoundedCornerShape(10.dp)
                )
                .clickable {
                  onSelectClip(clip)
                  onDismiss()
                }
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = clip.name,
                  color = ReVideoTextHigh,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "${clip.duration} • ${clip.resolution} • ${clip.motionStreamsCount} Motion Stream",
                  color = ReVideoTextMedium,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
              if (isSelected) {
                Icon(
                  Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = ReVideoPrimaryLight,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
