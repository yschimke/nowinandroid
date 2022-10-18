import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.android.horologist.compose.previews.WearLargeRoundDevicePreview
import com.google.wear.jetfit.compose.theme.JetFitColorPalette
import com.google.wear.jetfit.reports.SampleData
import com.google.wear.jetfit.reports.WeeklyProgressReport
import java.time.DayOfWeek
import androidx.compose.ui.graphics.Paint as ComposePaint

val textPaint = ComposePaint().apply {
    color = Color.White
}


val barPaint = ComposePaint().apply {
    color = Color(JetFitColorPalette.primary.toArgb())
    strokeWidth = 7f
    strokeCap = StrokeCap.Round
}

@Composable
fun GoalChart(
    modifier: Modifier = Modifier,
    state: WeeklyProgressReport
) {
    Canvas(modifier = modifier) {
        goalChart(state, textPaint, barPaint)
    }
}

val days = DayOfWeek.values()

fun DrawScope.goalChart(state: WeeklyProgressReport, textPaint: ComposePaint, barPaint: ComposePaint) {
    val androidPaint = textPaint.asFrameworkPaint()
    val width = size.width

    val dailyTotals = state.dailyTotals

    val max = dailyTotals.values.maxOrNull() ?: 10.0
    this.drawIntoCanvas {
        days.forEachIndexed { i, day ->
            val value = dailyTotals.getOrDefault(day, 0.0)

            val x = 50f + (width / 9) * i
            it.nativeCanvas.drawText(i.toString(), x, size.height * 0.9f, androidPaint)
            val height = 0.6f * (value.toFloat() / max) * size.height
            val bottom = size.height * 0.9f - 20f
            it.drawLine(
                Offset(x, bottom),
                Offset(x, (bottom - height).toFloat()),
                barPaint
            )
        }
    }
}

@WearLargeRoundDevicePreview
@Composable
fun GoalChartPreview() {
    val state = remember { SampleData.Weekly }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GoalChart(
            modifier = Modifier
                .size(160.dp, 100.dp)
                .border(1.dp, Color.White), state = state
        )
    }
}