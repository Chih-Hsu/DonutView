package com.example.customview.donutview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.customview.R
import com.example.customview.ext.toDp
import com.example.customview.ext.toPx
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class DonutView : View {

    private var value = 0
    private var padding = 0.toDp()
    private var isMoreThan90 = false

    // Indicator
    private var indicatorX = 0f
    private var indicatorY = 0f
    private var indicatorRadius = 50f
    private var lastX = 0f
    private var lastY = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        if (attrs != null) {
            val attrArray = R.styleable.DonutView
            val typedArray = context.theme.obtainStyledAttributes(attrs, attrArray, 0, 0)
            value = typedArray.getInt(R.styleable.DonutView_donutValue, 0)
            padding = typedArray.getInt(R.styleable.DonutView_padding, 0).toPx()

            typedArray.recycle()
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

    private val valueTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 200f
    }

    private val indicatorPaint = Paint().apply {
        color = context.getColor(R.color.purple_500)
    }

    private val valueCirclePaint = Paint().apply {
        color = context.getColor(R.color.purple_700)
        strokeWidth = 16.toPx().toFloat()
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val backgroundCirclePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 16.toPx().toFloat()
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    init {

        this.setOnTouchListener { v, event ->
            v.performClick()
            // canvas was translated but event not
            val translatedX = event.x - width / 2
            val translatedY = event.y - height / 2
            if (translatedX < indicatorX + indicatorRadius && translatedX > indicatorX - indicatorRadius && translatedY < indicatorY + indicatorRadius && translatedY > indicatorY - indicatorRadius) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x
                        lastY = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.x - lastX
                        val deltaY = event.y - lastY
                        val newX = indicatorX + deltaX
                        val newY = indicatorY + deltaY

                        lastX = event.x
                        lastY = event.y
                        val rad = atan2(newY.toDouble(), newX.toDouble())
                        val degree = Math.toDegrees(rad)
                        val angle = correctionAngle(degree)

                        setValueByAngle(angle)
                        setIndicatorXY(rad)
                        invalidate()
                    }
                    MotionEvent.ACTION_UP -> Log.d("Custt", "up")
                }
            }
            true
        }
    }

    private fun setValueByAngle(angle: Int) {
        // There are two possibility of value when degree is 360
        // 0 or 100 , so need a flag check which one is right
        isMoreThan90 = value > 90
        value = if (angle == 0 && isMoreThan90) {
            100
        } else if (angle == 0 && !isMoreThan90) {
            0
        } else {
            (angle / 360f * 100f).toInt()
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun setIndicatorXY(rad: Double) {
        val r = getMaxWidth().toFloat() / 2 - padding.toFloat()
        indicatorX = r * cos(rad).toFloat()
        indicatorY = r * sin(rad).toFloat()

    }

    private fun correctionAngle(degree: Double): Int {
        val angle = when (val result = degree.roundToInt()) {
            90 -> 0
            in 91..180 -> result - 90
            in 0..90 -> result + 270
            in -1 downTo -180 -> result + 270
            else -> 0
        }
        return angle
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)

        indicatorY = getMaxWidth().toFloat() / 2 - padding.toFloat()
        indicatorX = 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 為了取得四種象限，將整個坐標系的中點移到畫面的正中間
        canvas.translate(width.toFloat() / 2, height.toFloat() / 2)

        drawValue(canvas)
        drawBackgroundCircle(canvas)
        drawValueCircle(canvas)
        drawIndicator(canvas)
    }

    private fun drawBackgroundCircle(canvas: Canvas) {
        val radius: Float = getMaxWidth().toFloat() / 2

        canvas.drawArc(
            -radius + padding.toFloat(),
            -radius + padding.toFloat(),
            radius - padding.toFloat(),
            radius - padding.toFloat(),
            90f,
            360f,
            false,
            backgroundCirclePaint
        )
    }

    private fun drawValue(canvas: Canvas) {
        val textHeight = (valueTextPaint.fontMetrics.top - valueTextPaint.fontMetrics.bottom)

        canvas.drawText(value.toString(), 0f, -textHeight / 4, valueTextPaint)
    }

    private fun drawIndicator(canvas: Canvas) {

        canvas.drawCircle(indicatorX, indicatorY, indicatorRadius, indicatorPaint)
    }

    private fun drawValueCircle(canvas: Canvas) {
        val radius = getMaxWidth() / 2

        val angle = (value.toFloat() / 100) * 360f
        canvas.drawArc(
            -radius + padding.toFloat(),
            -radius + padding.toFloat(),
            radius - padding.toFloat(),
            radius - padding.toFloat(),
            90f,
            angle,
            false,
            valueCirclePaint
        )
    }

//    fun add() {
//        if (value < 100) {
//            value++
//            postInvalidate()
//        }
//    }
//
//    fun minus() {
//        if (value > 0) {
//            value--
//            postInvalidate()
//        }
//    }

    fun getValue() = value

    private fun getMaxWidth(): Int {
        return if (width > height) height else width
    }
}