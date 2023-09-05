package presentation

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.mariuszgromada.math.mxparser.License
import repos.Repository


fun main() = application {
    License.iConfirmNonCommercialUse("Dragontino")
    val repository = Repository()

    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource("app_icon.jpg"),
        title = AppName
    ) {
        ScaffoldView(repository, exitApp = ::exitApplication)
    }
}
