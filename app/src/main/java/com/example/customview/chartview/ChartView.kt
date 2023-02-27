package com.example.customview.chartview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customview.R
import com.example.customview.ext.toPx

class ChartView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        if (attrs != null) {
//            val attrArray = intArrayOf(android.R.attr.textColor)
//            val typedArray = context.theme.obtainStyledAttributes(attrs, attrArray, 0, 0)
//
//            val textColor = typedArray.getColor(0, Color.BLACK)
//            valueTextPaint.color = textColor
//            labelTextPaint.color = textColor
//
//            typedArray.recycle()
//        }
        if (attrs != null) {
            val attrArray = R.styleable.ChartView
            val typedArray = context.theme.obtainStyledAttributes(attrs, attrArray, 0, 0)

            val textColor = typedArray.getColor(R.styleable.ChartView_textColor, Color.BLACK)
            valueTextPaint.color = textColor
            typedArray.recycle()

            val nativeAttrArray = intArrayOf(android.R.attr.textColor)
            val nativeTypedArray =
                context.theme.obtainStyledAttributes(attrs, nativeAttrArray, 0, 0)
            val nativeTextColor = nativeTypedArray.getColor(0, Color.BLACK)
            labelTextPaint.color = nativeTextColor

        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    // Bar寬度
    private val barWidth = 30.toPx()

    // Bar間距
    private val barDistance = 30.toPx()

    // 最大值
    private val maxValue = 100

    private val textPadding = 10.toPx()


    private var barDatas: List<BarData> = listOf(
        BarData("2000", 23f),
        BarData("2001", 34f),
        BarData("2002", 10f),
        BarData("2003", 93f),
        BarData("2004", 77f),
    )

    private val paint = Paint().apply {
        color = Color.BLUE
    }
    private val linePaint = Paint().apply {
        //設定線條寬度
        strokeWidth = 8.toPx().toFloat()
        color = Color.GRAY
    }

    private val valueTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16.toPx().toFloat()
        textAlign = Paint.Align.RIGHT
    }

    private val labelTextPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16.toPx().toFloat()
        textAlign = Paint.Align.CENTER
    }

    private val valueWidth = getValueWidth()

    private val labelHeight = getLabelHeight()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBar(canvas, paint)
        drawX(canvas, linePaint)
        drawY(canvas, linePaint)
        drawValue(canvas)
        drawLabels(canvas)
    }

    private fun drawBar(canvas: Canvas, paint: Paint) {
        barDatas.forEachIndexed { index, barData ->
            val left = index * (barWidth + barDistance) + valueWidth
            val right = left + barWidth
            //底部
            val bottom = height - labelHeight
            val top = bottom * (1 - barData.value / maxValue)
            canvas.drawRect(left.toFloat(), top, right.toFloat(), bottom.toFloat(), paint)
        }
    }

    private fun drawY(canvas: Canvas, paint: Paint) {
        val startX = valueWidth
        val startY = 0f
        val stopX = valueWidth
        val stopY = height.toFloat() - labelHeight
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }

    private fun drawX(canvas: Canvas, paint: Paint) {
        val startX = valueWidth
        val startY = height.toFloat() - labelHeight
        val stopX = width.toFloat() + valueWidth
        val stopY = height.toFloat() - labelHeight
        canvas.drawLine(startX, startY, stopX, stopY, paint)
    }

    private fun drawValue(canvas: Canvas) {
        val x = valueWidth - textPadding
        val fontMetrics = valueTextPaint.fontMetrics
        val y = -fontMetrics.top
        // 只畫 maxValue
        canvas.drawText(maxValue.toString(), x, y, valueTextPaint)
    }

    private fun drawLabels(canvas: Canvas) {
        barDatas.forEachIndexed { index, barData ->
            val x = valueWidth + index * (barWidth + barDistance) + barWidth / 2
            val fontMetrics = labelTextPaint.fontMetrics
            val y = height - fontMetrics.bottom + textPadding
            canvas.drawText(barData.name, x, y, labelTextPaint)
        }
    }

    private fun getValueWidth(): Float {
//        return 50.toPx().toFloat()
        val rect = Rect()
        valueTextPaint.getTextBounds(maxValue.toString(), 0, maxValue.toString().length, rect)
        return rect.width().toFloat()
    }

    private fun getLabelHeight(): Float {
//        return 50.toPx().toFloat()
        val metrics = labelTextPaint.fontMetrics
        Log.d("Custt", "top = ${metrics.top}  bottom = ${metrics.bottom}")
        return metrics.bottom - metrics.top
    }
}