package repos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mariuszgromada.math.mxparser.Expression
import presentation.IncorrectBoundsException
import presentation.IncorrectFunctionException
import presentation.NoResult
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

class Repository {
    suspend fun calculateMinWidth(
        sigma: Double,
        interval: ClosedFloatingPointRange<Double>,
        functionFormula: String,
        primitiveFunctionFormula: String,
        signsCount: Int,
    ): Result<OutputResult> = withContext(Dispatchers.Default) {
        if (interval.endInclusive < interval.start) {
            return@withContext Result.failure(IndexOutOfBoundsException(IncorrectBoundsException))
        }

        val absoluteSquare = with(primitiveFunctionFormula) {
            abs(executeFunction(interval.endInclusive) - executeFunction(interval.start))
        }

        var rectsCount = 1

        if (absoluteSquare.isNaN() || absoluteSquare.isInfinite()) {
            return@withContext Result.failure(Exception(IncorrectFunctionException))
        }

        do {
            var leftPoint = interval.start
            val currRectWidth: Double = (interval.endInclusive - interval.start) / rectsCount
            var currSquare = 0.0

            while (leftPoint + currRectWidth <= interval.endInclusive) {
                val currHeight = with(functionFormula) {
                    minOf(executeFunction(leftPoint), executeFunction(leftPoint + currRectWidth))
                }
                currSquare += currHeight
                leftPoint += currRectWidth
            }

            currSquare *= currRectWidth

            if (abs(currSquare - absoluteSquare) < sigma * absoluteSquare) {
                return@withContext Result.success(
                    value = OutputResult(
                        width = currRectWidth.round(signsCount),
                        rectsCount = rectsCount,
                        square = currSquare.round(signsCount),
                    ),
                )
            }

            rectsCount++
        } while (currRectWidth.round(signsCount) > 0.0)

        return@withContext Result.failure(Exception(NoResult))
    }


    private fun String.executeFunction(x: Double): Double {
        val replacedFormula = replace(
            oldValue = "x",
            newValue = " $x ",
            ignoreCase = true
        )

        val expression = Expression(replacedFormula)
        return expression.calculate()
    }



    private fun Double.round(countSigns: Int = 1) =
        BigDecimal(this).setScale(countSigns, RoundingMode.HALF_UP).toDouble()
}