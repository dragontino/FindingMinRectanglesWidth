package presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import repos.OutputResult
import repos.Repository

class ViewModel(private val repository: Repository) {
    var startPointOfRange by mutableStateOf("")
    var endPointOfRange by mutableStateOf("")
    var functionFormula by mutableStateOf("")
    var primitiveFunctionFormula by mutableStateOf("")
    var sigma by mutableStateOf("")
    var signsCount by mutableStateOf("2")
    var result: OutputResult? by mutableStateOf(null)

    var startPointError by mutableStateOf(false)
    var endPointError by mutableStateOf(false)
    var sigmaError by mutableStateOf(false)
    var signsCountError by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var showDialog by mutableStateOf(false)
    var isDarkTheme by mutableStateOf(false)



    suspend fun calculateResult(): Result<OutputResult> {
        isLoading = true
        delay(100)

        checkDouble(startPointOfRange) {
            startPointError = it != null
            if (it != null) return Result.failure(Exception(it))
        }

        checkDouble(endPointOfRange) { msg ->
            endPointError = msg != null
            if(msg != null) return Result.failure(Exception(msg))
        }

        if (startPointOfRange.toDouble() > endPointOfRange.toDouble()) {
            startPointError = true
            endPointError = true
            return Result.failure(Exception(IncorrectBoundsException))
        }

        checkDouble(sigma) {
            sigmaError = it != null
            if (it != null) return Result.failure(Exception(it))
        }

        checkDouble(signsCount) {
            signsCountError = it != null
            if (it != null) return Result.failure(Exception(it))
        }

        val result = repository.calculateMinWidth(
            sigma = sigma.toDouble(),
            interval = startPointOfRange.toDouble()..endPointOfRange.toDouble(),
            functionFormula = functionFormula,
            primitiveFunctionFormula = primitiveFunctionFormula,
            signsCount = signsCount.toInt()
        )

        delay(100)
        isLoading = false
        return result
    }


    private inline fun checkDouble(stringDouble: String, resultMessage: (String?) -> Unit) {
        resultMessage(
            when {
                stringDouble.isBlank() -> EmptyFieldException
                stringDouble.toDoubleOrNull() == null -> IncorrectValueException
                else -> null
            }
        )
    }
}