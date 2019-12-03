package id.co.barchartresearch

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listData by lazy {
        mutableListOf(
            ChartData("1/9", 60.5F),
            ChartData("2/9", 72.5F),
            ChartData("3/9", 80.75F),
            ChartData("4/9", 67.2F),
            ChartData("5/9", 88.5F),
            ChartData("6/9", 85.3F),
            ChartData("7/9", 70.5F)
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

    private val onValueSelectedRectF by lazy {
        RectF()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChart()
    }

    private fun initChart() {
        with(bar_chart) {
            description.isEnabled = false
            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false

            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            animateY(1500)

            xAxis.apply {
                position = XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = whiteColor
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return listData[0].date
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
            }

            val barChartRender = CustomBarChartRender(this, animator, viewPortHandler).apply {
                setRadius(50)
            }
            renderer = barChartRender

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {

                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    if (e == null) return

                    val bounds: RectF = onValueSelectedRectF
                    bar_chart.getBarBounds(e as BarEntry?, bounds)
                    val position: MPPointF = bar_chart.getPosition(e, AxisDependency.LEFT)

                    MPPointF.recycleInstance(position)
                }
            })
        }
        setData()
    }

    private fun setData() {
        val values = mutableListOf<BarEntry>()
        var index = 1
        listData.forEach {
            index++
            values.add(BarEntry(index.toFloat(), it.value))
        }

        val barDataSet = BarDataSet(values, "").apply {
            setDrawIcons(false)
            setDrawValues(false)
            gradientColors = barGradientColor
        }

        val dataSets = mutableListOf(barDataSet)
        val data = BarData(dataSets as List<IBarDataSet>?).apply {
            setValueTextSize(10F)
            barWidth = 0.6F
        }

        bar_chart.data = data
    }
}
