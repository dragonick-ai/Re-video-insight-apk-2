package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ReVideoBorderSubtle
import com.example.ui.theme.ReVideoCanvas
import com.example.ui.theme.ReVideoPrimaryContainer
import com.example.ui.theme.ReVideoPrimaryLight
import com.example.ui.theme.ReVideoTextMedium
import com.example.viewmodel.AppTab

@Composable
fun ReVideoBottomNav(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(ReVideoCanvas.copy(alpha = 0.98f))
      .border(1.dp, ReVideoBorderSubtle)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavItem(
        tab = AppTab.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        isSelected = currentTab == AppTab.HOME,
        onClick = { onTabSelected(AppTab.HOME) }
      )
      NavItem(
        tab = AppTab.CREATE,
        label = "Create",
        selectedIcon = Icons.Filled.Videocam,
        unselectedIcon = Icons.Outlined.Videocam,
        isSelected = currentTab == AppTab.CREATE,
        onClick = { onTabSelected(AppTab.CREATE) }
      )
      NavItem(
        tab = AppTab.HISTORY,
        label = "History",
        selectedIcon = Icons.Filled.Restore,
        unselectedIcon = Icons.Outlined.Restore,
        isSelected = currentTab == AppTab.HISTORY,
        onClick = { onTabSelected(AppTab.HISTORY) }
      )
      NavItem(
        tab = AppTab.SETTINGS,
        label = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        isSelected = currentTab == AppTab.SETTINGS,
        onClick = { onTabSelected(AppTab.SETTINGS) }
      )
    }
  }
}

@Composable
private fun NavItem(
  tab: AppTab,
  label: String,
  selectedIcon: ImageVector,
  unselectedIcon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val testTag = "nav_${tab.route}"
  Column(
    modifier = Modifier
      .size(width = 64.dp, height = 48.dp)
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (isSelected) {
      Box(
        modifier = Modifier
          .size(width = 40.dp, height = 24.dp)
          .clip(RoundedCornerShape(9999.dp))
          .background(ReVideoPrimaryContainer.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = selectedIcon,
          contentDescription = label,
          tint = ReVideoPrimaryLight,
          modifier = Modifier.size(19.dp)
        )
      }
    } else {
      Icon(
        imageVector = unselectedIcon,
        contentDescription = label,
        tint = ReVideoTextMedium,
        modifier = Modifier.size(20.dp)
      )
    }
    Text(
      text = label,
      color = if (isSelected) ReVideoPrimaryLight else ReVideoTextMedium,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}
