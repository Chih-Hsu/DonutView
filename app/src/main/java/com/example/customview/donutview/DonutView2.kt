package com.example.customview.donutview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.customview.ext.toPx
import kotlin.random.Random

class DonutView2 : View {

    private var padding = 30.toPx()
    private val dataList = mutableListOf<Float>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
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

    private val backgroundCirclePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 16.toPx().toFloat()
    }
    private val valueArcPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 16.toPx().toFloat()
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {

        canvas.translate(width.toFloat() / 2, height.toFloat() / 2)
        drawBackGroundCircle(canvas)
        drawValue(canvas)
    }

    private fun drawValue(canvas: Canvas) {
        var total = 0f
        val percentList = mutableMapOf<Int, Float>()
        val radius: Float = getMaxWidth().toFloat() / 2

        dataList.forEach {
            total += it
        }

        dataList.forEachIndexed { index, value ->
            percentList.put(index, value / total)

        }
        var lastAngle = 0f
        percentList.forEach {
            val degree = it.value * 360f
            val startAngle = lastAngle
            Log.d("TTT","degree = $degree  startAngle = $startAngle")
            lastAngle = startAngle + degree
            val newColor =
                Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            Log.d("TTT","color = ${Random.nextInt(256)}")
            valueArcPaint.color = newColor
            canvas.drawArc(
                -radius + padding.toFloat(),
                -radius + padding.toFloat(),
                radius - padding.toFloat(),
                radius - padding.toFloat(),
                startAngle,
                degree,
                false,
                valueArcPaint
            )
        }
    }

    private fun drawBackGroundCircle(canvas: Canvas) {
        val radius: Float = getMaxWidth().toFloat() / 2
        canvas.drawCircle(
            0f,
            0f,
            radius - padding.toFloat(),
            backgroundCirclePaint
        )
    }

    fun submitList(list: List<Float>) {
        dataList.clear()
        dataList.addAll(list)
        postInvalidate()
    }

    private fun getMaxWidth(): Int {
        return if (width > height) height else width
    }
}