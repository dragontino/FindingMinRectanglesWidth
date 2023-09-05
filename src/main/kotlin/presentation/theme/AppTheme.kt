package presentation.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val darkColorShame = darkColorScheme(
    primary = Color(0xFF8F9101),
    onPrimary = Color.White,
    background = Color(0xFF212123),
    onBackground = Color.White,
    surface = Color(red = 56, green = 43, blue = 50),
    onSurface = Color.White,
    error = Color.Red.copy(alpha = .8f)
)


private val lightColorShame = lightColorScheme(
    primary = Color(0xFFD9D214),
    onPrimary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(red = 249, green = 229, blue = 237),
    onSurface = Color.Black,
    error = Color.Red
)


private val typography = Typography(
    bodyLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = FontFamily(RobotoFont)
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily(RobotoFont),
        lineHeight = 22.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(RobotoFont),
        lineHeight = 17.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(VerdanaFont),
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(VerdanaFont),
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.2.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(VerdanaFont),
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(MathFont),
        fontSize = 32.sp,
        fontWeight = FontWeight.Light
    ),
)


@Composable
fun AppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorShame else lightColorShame,
        typography = typography,
        content = content
    )
}


@Composable
fun Color.animate(durationMillis: Int = 400): Color = animateColorAsState(
    targetValue = this,
    animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
).value