package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.components.CreditsDialog
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.ProfileDialog
import com.example.ui.components.ReVideoBottomNav
import com.example.ui.components.ReVideoTopBar
import com.example.ui.components.ToastNotification
import com.example.ui.screens.CreateCombineScreen
import com.example.ui.screens.CreateUploadScreen
import com.example.ui.screens.GenerationResultScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ReVideoCanvas
import com.example.viewmodel.AppTab
import com.example.viewmodel.CreateStep
import com.example.viewmodel.ReVideoViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: ReVideoViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        ReVideoApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ReVideoApp(viewModel: ReVideoViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val createStep by viewModel.createStep.collectAsState()
  val credits by viewModel.credits.collectAsState()
  val videoMinsLeft by viewModel.videoMinsLeft.collectAsState()
  val activeProject by viewModel.activeProject.collectAsState()
  val toastEvent by viewModel.toastEvent.collectAsState()
  val showCreditDialog by viewModel.showCreditDialog.collectAsState()
  val showNotificationDialog by viewModel.showNotificationDialog.collectAsState()
  val showProfileDialog by viewModel.showProfileDialog.collectAsState()

  // Handle native Android back button
  BackHandler(enabled = currentTab != AppTab.HOME || createStep != CreateStep.UPLOAD_ANALYZE) {
    if (currentTab == AppTab.CREATE) {
      when (createStep) {
        CreateStep.GENERATE -> viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
        CreateStep.COMBINE_IDEA -> viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
        CreateStep.UPLOAD_ANALYZE -> viewModel.selectTab(AppTab.HOME)
      }
    } else {
      viewModel.selectTab(AppTab.HOME)
    }
  }

  // Active top bar configuration based on active tab and step
  val (topTitle, topSubtitle, showBack) = when (currentTab) {
    AppTab.HOME -> Triple("ReVideo AI", "Neural Video Studio", false)
    AppTab.CREATE -> when (createStep) {
      CreateStep.UPLOAD_ANALYZE -> Triple("ReVideo AI", "Create Studio", false)
      CreateStep.COMBINE_IDEA -> Triple("ReVideo AI", "Prompt Enhancer", true)
      CreateStep.GENERATE -> Triple(activeProject?.title ?: "Video Generation", "Gen-3 V2V Engine • 1080p 60fps", true)
    }
    AppTab.HISTORY -> Triple("ReVideo AI", "Neural Video Studio", false)
    AppTab.SETTINGS -> Triple("ReVideo AI", "Studio Settings", false)
  }

  Scaffold(
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      ReVideoTopBar(
        title = topTitle,
        subtitle = topSubtitle,
        credits = credits,
        showBackButton = showBack,
        onBackClick = {
          if (currentTab == AppTab.CREATE) {
            when (createStep) {
              CreateStep.GENERATE -> viewModel.setCreateStep(CreateStep.COMBINE_IDEA)
              CreateStep.COMBINE_IDEA -> viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
              CreateStep.UPLOAD_ANALYZE -> viewModel.selectTab(AppTab.HOME)
            }
          } else {
            viewModel.selectTab(AppTab.HOME)
          }
        },
        onCreditsClick = { viewModel.setCreditDialogVisible(true) },
        onNotificationsClick = { viewModel.setNotificationDialogVisible(true) },
        onProfileClick = { viewModel.setProfileDialogVisible(true) }
      )
    },
    bottomBar = {
      ReVideoBottomNav(
        currentTab = currentTab,
        onTabSelected = { tab ->
          viewModel.selectTab(tab)
          if (tab == AppTab.CREATE && createStep == CreateStep.GENERATE && activeProject == null) {
            viewModel.setCreateStep(CreateStep.UPLOAD_ANALYZE)
          }
        }
      )
    },
    containerColor = ReVideoCanvas,
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(ReVideoCanvas),
      contentAlignment = Alignment.TopCenter
    ) {
      // Centered adaptive max width container for tablet / foldable support
      Box(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 600.dp)
      ) {
        when (currentTab) {
          AppTab.HOME -> HomeScreen(viewModel = viewModel)
          AppTab.CREATE -> when (createStep) {
            CreateStep.UPLOAD_ANALYZE -> CreateUploadScreen(viewModel = viewModel)
            CreateStep.COMBINE_IDEA -> CreateCombineScreen(viewModel = viewModel)
            CreateStep.GENERATE -> GenerationResultScreen(viewModel = viewModel)
          }
          AppTab.HISTORY -> HistoryScreen(viewModel = viewModel)
          AppTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
        }

        // Animated drop-down toast notification
        ToastNotification(
          toastEvent = toastEvent,
          onDismiss = { viewModel.dismissToast() },
          modifier = Modifier.align(Alignment.TopCenter)
        )
      }

      // Dialogs
      if (showCreditDialog) {
        CreditsDialog(
          credits = credits,
          onDismiss = { viewModel.setCreditDialogVisible(false) },
          onTopUp = { amount -> viewModel.addCredits(amount) }
        )
      }

      if (showNotificationDialog) {
        NotificationsDialog(
          onDismiss = { viewModel.setNotificationDialogVisible(false) }
        )
      }

      if (showProfileDialog) {
        ProfileDialog(
          credits = credits,
          videoMinsLeft = videoMinsLeft,
          onDismiss = { viewModel.setProfileDialogVisible(false) }
        )
      }
    }
  }
}
