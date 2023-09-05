package presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import presentation.theme.VerdanaFont
import presentation.theme.animate

@Composable
fun MainContent(
    viewModel: ViewModel,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(
        contentPadding = PaddingValues(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() }
            )
            .fillMaxSize(),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Интервал:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.animate(),
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    InputTextField(
                        value = viewModel.startPointOfRange,
                        onValueChange = viewModel::startPointOfRange::set,
                        hint = "0.2",
                        leadingText = "от",
                        isError = viewModel.startPointError
                    )

                    InputTextField(
                        value = viewModel.endPointOfRange,
                        onValueChange = viewModel::endPointOfRange::set,
                        hint = "10.7",
                        leadingText = "до",
                        isError = viewModel.endPointError
                    )
                }
            }
        }

        item {
            InputTextField(
                value = viewModel.functionFormula,
                onValueChange = viewModel::functionFormula::set,
                prefixText = "f(x) = ",
                prefixTextStyle = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                supportingText = "Исходная функция"
            )
        }

        item {
            InputTextField(
                value = viewModel.primitiveFunctionFormula,
                onValueChange = viewModel::primitiveFunctionFormula::set,
                prefixText = "F(x) = ",
                prefixTextStyle = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                supportingText = "Первообразная исходной функции"
            )
        }

        item {
            InputTextField(
                value = viewModel.sigma,
                onValueChange = viewModel::sigma::set,
                prefixText = "σ = ",
                prefixTextStyle = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                isError = viewModel.sigmaError
            )
        }

        item {
            InputTextField(
                value = viewModel.signsCount,
                onValueChange = viewModel::signsCount::set,
                supportingText = "Число знаков после запятой",
                isError = viewModel.signsCountError
            )
        }
    }

    val result = viewModel.result
    if (result != null) {
        val coloredStyle = SpanStyle(
            color = MaterialTheme.colorScheme.primary.animate(),
            fontWeight = FontWeight.Bold
        )

        SelectionContainer {
            Text(
                text = buildAnnotatedString {
                    append("Минимальная ширина прямоугольников = ")
                    withStyle(coloredStyle) {
                        append(result.width.toString())
                    }

                    append("\nКоличество прямоугольников = ")
                    withStyle(coloredStyle) {
                        append(result.rectsCount.toString())
                    }

                    append("\nИх суммарная площадь = ")
                    withStyle(coloredStyle) {
                        append(result.square.toString())
                    }
                },
                color = MaterialTheme.colorScheme.onBackground.animate(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}




@Composable
private fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingText: String = "",
    prefixText: String = "",
    prefixTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    hint: String = "",
    supportingText: String = "",
    isError: Boolean = false,

) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        isError = isError,
        leadingIcon = if (leadingText.isNotBlank()) {
            {
                Text(
                    text = leadingText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(VerdanaFont)
                )
            }
        } else null,
        prefix = {
            Text(text = prefixText, style = prefixTextStyle)
        },
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.labelMedium
            )
        },
        supportingText = {
            if (supportingText.isNotBlank()) {
                Text(text = supportingText, style = MaterialTheme.typography.bodySmall)
            }
        },
        keyboardActions = KeyboardActions {
            focusManager.moveFocus(FocusDirection.Next)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground.animate(),
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground.animate(),
            errorTextColor = MaterialTheme.colorScheme.error.animate(),
            focusedBorderColor = MaterialTheme.colorScheme.primary.animate(),
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.animate(),
            errorBorderColor = MaterialTheme.colorScheme.error.animate(),
            focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.animate(),
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.animate(),
            focusedSupportingTextColor = MaterialTheme.colorScheme.primary.animate(),
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.onBackground.animate(),
            errorSupportingTextColor = MaterialTheme.colorScheme.error.animate(),
            cursorColor = MaterialTheme.colorScheme.onBackground.animate(),
            errorCursorColor = MaterialTheme.colorScheme.error.animate(),
            focusedPlaceholderColor = Color.LightGray,
            unfocusedPlaceholderColor = Color.LightGray
        ),
        modifier = modifier
    )
}