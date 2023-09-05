package presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import presentation.theme.AppTheme
import presentation.theme.animate
import repos.Repository
import java.awt.Desktop
import java.net.URI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(repository: Repository, exitApp: () -> Unit) {
    val viewModel = rememberSaveable { ViewModel(repository) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showSnackbar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Закрыть",
                duration = SnackbarDuration.Short
            )
        }
        Unit
    }

    AppTheme(viewModel.isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = AppName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = exitApp) {
                            Icon(Icons.Rounded.Logout, contentDescription = "exit from app")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.showDialog = !viewModel.showDialog }) {
                            Icon(
                                imageVector = Icons.Outlined.ContactSupport,
                                contentDescription = "support"
                            )
                        }

                        Spacer(Modifier.size(8.dp))

                        IconButton(
                            onClick = { viewModel.isDarkTheme = !viewModel.isDarkTheme }
                        ) {
                            Crossfade(
                                targetState = viewModel.isDarkTheme,
                                animationSpec = tween(durationMillis = 400, easing = LinearEasing)
                            ) { isDark ->
                                Icon(
                                    imageVector = when {
                                        isDark -> Icons.Outlined.LightMode
                                        else -> Icons.Outlined.DarkMode
                                    },
                                    contentDescription = "Switch mode"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.animate(),
                        titleContentColor = MaterialTheme.colorScheme.onPrimary.animate(),
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary.animate()
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) {
                    Snackbar(
                        snackbarData = it,
                        actionColor = MaterialTheme.colorScheme.primary.animate(),
                        containerColor = MaterialTheme.colorScheme.onBackground.animate(),
                        contentColor = MaterialTheme.colorScheme.background.animate(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = {
                           Text(text = "Вычислить значения", style = MaterialTheme.typography.bodyMedium)
                    },
                    icon = {
                        Icon(imageVector = Icons.Outlined.Calculate, contentDescription = "calculate values")
                    },
                    onClick = {
                        if (!viewModel.isLoading) {
                            scope.launch {
                                val result = viewModel.calculateResult()
                                result.exceptionOrNull()?.message?.let(showSnackbar)
                                result.getOrNull()?.let { viewModel.result = it }
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary.animate(),
                    contentColor = MaterialTheme.colorScheme.onPrimary.animate()
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = MaterialTheme.colorScheme.background.animate(),
            contentColor = MaterialTheme.colorScheme.onBackground.animate(),
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            if (viewModel.showDialog) {
                FeedbackDialog(
                    onCloseDialog = { viewModel.showDialog = false },
                    showSnackbar = showSnackbar
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MainContent(
                    modifier = Modifier.padding(contentPadding),
                    viewModel = viewModel
                )

                Crossfade(
                    targetState = viewModel.isLoading,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                ) { state ->
                    if (state) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .alpha(.8f)
                                .background(MaterialTheme.colorScheme.background.animate())
                                .fillMaxSize(),
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary.animate(),
                                modifier = Modifier.scale(2f),
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 3.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}





@Composable
private fun FeedbackDialog(onCloseDialog: () -> Unit, showSnackbar: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onCloseDialog,
        confirmButton = {
            TextButton(
                onClick = onCloseDialog,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground.animate()
                )
            ) {
                Text(
                    text = "Закрыть",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        title = {
            Text(
                text = "Обратная связь",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
               Column(
                   verticalArrangement = Arrangement.spacedBy(8.dp),
                   horizontalAlignment = Alignment.CenterHorizontally,
                   modifier = Modifier
                       .padding(horizontal = 4.dp)
                       .verticalScroll(rememberScrollState())
               ) {
                   val urls = arrayOf(
                       FeedbackData(
                           name = "ВКонтакте",
                           logoName = "vk_icon.png",
                           url = "https://vk.com/sergiovonpeter"
                       ),
                       FeedbackData(
                           name = "Telegram",
                           logoName = "telegram_icon.png",
                           url = "https://t.me/cepetroff"
                       )
                   )

                   urls.forEach { (name, logo, url) ->
                       Row(
                           modifier = Modifier
                               .clip(RoundedCornerShape(10.dp))
                               .clickable {
                                   openInBrowser(url) { result ->
                                       result.exceptionOrNull()?.message?.let { showSnackbar(it) }
                                   }
                               }
                               .padding(vertical = 16.dp, horizontal = 8.dp)
                               .fillMaxWidth(),
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Row(modifier = Modifier.weight(4f)) {
                               Image(
                                   painter = painterResource(logo),
                                   contentDescription = "logo",
                                   modifier = Modifier.width(25.dp)
                               )

                               Spacer(Modifier.size(16.dp))

                               Text(
                                   text = name,
                                   style = MaterialTheme.typography.bodyMedium
                               )
                           }

                           Icon(
                               imageVector = Icons.Rounded.NavigateNext,
                               contentDescription = null,
                               tint = MaterialTheme.colorScheme.primary.animate()
                           )
                       }
                   }
               }
        },
        containerColor = MaterialTheme.colorScheme.surface.animate(),
        textContentColor = MaterialTheme.colorScheme.onSurface.animate(),
        titleContentColor = MaterialTheme.colorScheme.onSurface.animate(),
    )
}



private fun openInBrowser(url: String, result: (Result<Unit>) -> Unit) {
    val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null

    if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
        result(
            Result.failure(Exception("Открытие ссылок не поддерживается вашей операционной системой!"))
        )
        return
    }

    try {
        desktop.browse(URI(url))
        result(Result.success(Unit))
    } catch (e: Exception) {
        result(Result.failure(e))
    }
}



private data class FeedbackData(
    val name: String,
    val logoName: String,
    val url: String
)



@Preview
@Composable
private fun ScaffoldViewPreview() {
    ScaffoldView(Repository()) {}
}



@Preview
@Composable
private fun FeedbackDialogPreview() {
    FeedbackDialog({}, {})
}