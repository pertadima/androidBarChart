package id.co.barchartresearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.model.GradientColor
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val START_RANDOM = 0
        private const val END_RANDOM = 100
    }

    private val listData by lazy {
        mutableListOf(
            ChartData("1/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("2/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("3/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("4/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("5/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("6/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("7/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat())
        )
    }

    private val newData by lazy {
        mutableListOf(
            ChartData("1/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("2/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("3/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("4/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("5/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("6/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("7/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("8/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("9/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("10/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("4/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("5/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("6/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("7/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("1/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("2/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("3/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("4/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("5/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("6/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat()),
            ChartData("7/9", Random.nextInt(START_RANDOM, END_RANDOM).toFloat())
        )
    }


    private val startColor by lazy {
        ContextCompat.getColor(this, android.R.color.holo_orange_dark)
    }
    private val endColor by lazy {
        ContextCompat.getColor(this, android.R.color.holo_orange_light)
    }

    private val whiteColor by lazy {
        ContextCompat.getColor(this, R.color.colorWhite)
    }

    private val transparentWhiteColor by lazy {
        ContextCompat.getColor(this, R.color.colorWhiteTransparent)
    }

    private val barGradientColor by lazy {
        mutableListOf(GradientColor(startColor, endColor))
    }

    private val customMarkerView by lazy {
        CustomMarketView(this, R.layout.item_marker_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChart()
        btn_update.setOnClickListener {
            setData(newData)
        }
    }

    private fun initChart() {
        customMarkerView.chartView = bar_chart
        with(bar_chart) {
            marker = customMarkerView
            description.isEnabled = false
            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false

            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)

            val barChartRender = CustomBarChartRender(this, animator, viewPortHandler).apply {
                setRadius(50)
            }
            renderer = barChartRender
        }
        setData(listData)
    }

    private fun setData(barData: List<ChartData>) {
        val values = mutableListOf<BarEntry>()
        barData.forEachIndexed { index, chartData ->
            values.add(BarEntry(index.toFloat(), chartData.value))
        }

        val barDataSet = BarDataSet(values, "").apply {
            setDrawValues(false)
            gradientColors = barGradientColor
            highLightAlpha = 0
        }

        val dataSets = mutableListOf(barDataSet)
        val data = BarData(dataSets as List<IBarDataSet>?).apply {
            setValueTextSize(10F)
            barWidth = 0.6F
        }

        with(bar_chart) {
            animateY(1500)
            xAxis.apply {
                position = XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = whiteColor
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return barData[value.toInt()].date
                    }
                }
            }

            axisRight.apply {
                textColor = whiteColor
                setDrawAxisLine(false)
                gridColor = transparentWhiteColor
                axisMinimum = 0F
                axisMaximum = 100F
            }
            axisLeft.apply {
                isEnabled = false
                gridColor = transparentWhiteColor
                axisMinimum = 0F
                axisMaximum = 100F
            }

            notifyDataSetChanged()
            this.data = data
            invalidate()
        }
    }
}
