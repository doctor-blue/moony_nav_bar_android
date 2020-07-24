package com.doctorblue.noname_library

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.XmlRes
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Create by Nguyen Van Tan 7/2020
 * */
class NoNameBottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.NoNameBottomBarStyle
) : View(context, attrs, defStyleAttr) {

    //Attributes

    @ColorInt
    var barBackground: Int = Color.WHITE
        set(@ColorInt value) {
            field = value
            paintBackground.color = barBackground
            invalidate()
        }

    @XmlRes
    var itemMenuRes: Int = INVALID_RES
        set(@XmlRes value) {
            field = value
            if (value != INVALID_RES) {
                items = BottomBarParser(context, value).parse()
                invalidate()
            }
        }


    @ColorInt
    var indicatorColor: Int = DEFAULT_INDICATOR_COLOR
        set(@ColorInt value) {
            field = value
            paintIndicator.color = value
            invalidate()
        }

    @Dimension
    var itemIconSize: Float = (24f * resources.displayMetrics.density).roundToInt().toFloat()
        set(@Dimension value) {
            field = value
            invalidate()
        }

    @ColorInt
    var itemIconTintActive: Int = Color.BLACK
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    @ColorInt
    var itemIconTint: Int = Color.GRAY
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    var indicatorType: IndicatorType = IndicatorType.LINE
        set(value) {
            field = value
            invalidate()
        }

    var indicatorPosition: IndicatorPosition = IndicatorPosition.BOTTOM
        set(value) {
            field = value
            invalidate()
        }

    var indicatorDuration: Long = 500


    var itemActiveIndex: Int = 0
        set(value) {
            field = value
            onItemSelectedListener?.onItemSelect(items[value].id)
            onItemSelected?.invoke(items[value].id)
            applyItemActiveIndex()
        }

    var isShow: Boolean = true


    private var indicatorLocation = 0f
    private val rect = RectF()
    private var itemWidth: Float = 0f
    private var itemHeight: Float = 0f
    private var items = listOf<BottomBarItem>()
    private var currentIconTint: Int = itemIconTintActive
    private var defaultY = 0f
    private var bottomMargin = 0f


    //Paint
    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val paintIndicator = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = indicatorColor
    }

    // Listeners
    var onItemSelectedListener: OnItemSelectedListener? = null

    var onItemReselectedListener: OnItemReselectedListener? = null

    var onItemSelected: ((Int) -> Unit)? = null

    var onItemReselected: ((Int) -> Unit)? = null

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NoNameBottomBar,
            defStyleAttr,
            0
        )
        try {

            barBackground = typedArray.getColor(
                R.styleable.NoNameBottomBar_backgroundColor,
                barBackground
            )
            itemMenuRes = typedArray.getResourceId(
                R.styleable.NoNameBottomBar_menu,
                itemMenuRes
            )

            indicatorColor =
                typedArray.getColor(R.styleable.NoNameBottomBar_indicatorColor, indicatorColor)

            itemIconSize = typedArray.getDimension(
                R.styleable.NoNameBottomBar_iconSize,
                itemIconSize
            )
            itemIconTintActive =
                typedArray.getColor(R.styleable.NoNameBottomBar_iconTintActive, itemIconTintActive)

            itemIconTint = typedArray.getColor(R.styleable.NoNameBottomBar_iconTint, itemIconTint)

            indicatorDuration =
                typedArray.getInt(R.styleable.NoNameBottomBar_duration, indicatorDuration.toInt())
                    .toLong()


            val indicatorType =
                typedArray.getInt(R.styleable.NoNameBottomBar_indicatorType, 0)

            this.indicatorType = if (indicatorType == 0) IndicatorType.LINE else IndicatorType.POINT

            val indicatorPosition =
                typedArray.getInt(R.styleable.NoNameBottomBar_indicatorPosition, 1)

            this.indicatorPosition =
                if (indicatorPosition == 0) IndicatorPosition.TOP else IndicatorPosition.BOTTOM

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val margins: ViewGroup.MarginLayoutParams? =
            ViewGroup.MarginLayoutParams::class.java.cast(layoutParams)
        margins?.let { bottomMargin = it.bottomMargin.toFloat() }
        defaultY = y
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var lastX = 0f
        itemWidth = w / items.size.toFloat()
        itemHeight = h.toFloat()

        for (item in items) {
            item.rect = RectF(lastX, 0f, itemWidth + lastX, height.toFloat())
            lastX += itemWidth
        }

        applyItemActiveIndex()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //Draw background
        canvas.drawRect(
            0f, 0f,
            width.toFloat(),
            height.toFloat(),
            paintBackground
        )


        //Draw indicator
        if (indicatorType == IndicatorType.LINE) {
            var top = itemHeight - itemIconSize / 10
            var bottom = itemHeight

            if (indicatorPosition == IndicatorPosition.TOP) {
                top = 0f
                bottom = itemIconSize / 10
            }

            rect.left = indicatorLocation + (itemWidth - itemIconSize) / 2
            rect.top = top
            rect.right = indicatorLocation + (itemWidth + itemIconSize) / 2
            rect.bottom = bottom

            canvas.drawRect(rect, paintIndicator)
        } else {
            val radius = itemIconSize / 11.0f
            val x = indicatorLocation + itemWidth / 2
            var y = itemHeight - radius * 3
            if (indicatorPosition == IndicatorPosition.TOP) y = radius

            canvas.drawCircle(x, y, radius, paintIndicator)
        }

        //Draw icon
        for ((index, item) in items.withIndex()) {

            item.icon.mutate()

            var top = height / 2 - itemIconSize.toInt() / 2
            var bottom = height / 2 + itemIconSize.toInt() / 2

            if (index == itemActiveIndex) {

                top -= if (indicatorPosition == IndicatorPosition.TOP) top / 4 else top / 3

                bottom = top + itemIconSize.toInt()
            }

            item.icon.setBounds(
                item.rect.centerX().toInt() - itemIconSize.toInt() / 2,
                top,
                item.rect.centerX().toInt() + itemIconSize.toInt() / 2,
                bottom
            )

            DrawableCompat.setTint(
                item.icon,
                if (index == itemActiveIndex) currentIconTint else itemIconTint
            )

            item.icon.draw(canvas)
        }
    }


    private fun applyItemActiveIndex() {
        if (items.isNotEmpty()) {
            for ((index, item) in items.withIndex()) {
                if (index == itemActiveIndex) {
                    animateAlpha(item, OPAQUE)
                } else {
                    animateAlpha(item, TRANSPARENT)
                }
            }

            ValueAnimator.ofFloat(
                indicatorLocation,
                items[itemActiveIndex].rect.left
            ).apply {
                duration = indicatorDuration
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    indicatorLocation = animation.animatedValue as Float
                }
                start()
            }

            ValueAnimator.ofObject(ArgbEvaluator(), itemIconTint, itemIconTintActive).apply {
                duration = indicatorDuration
                addUpdateListener {
                    currentIconTint = it.animatedValue as Int
                }
                start()
            }
        }
    }

    private fun animateAlpha(item: BottomBarItem, to: Int) {
        ValueAnimator.ofInt(item.alpha, to).apply {
            duration = indicatorDuration
            addUpdateListener {
                val value = it.animatedValue as Int
                item.alpha = value
                invalidate()
            }
            start()
        }
    }

    fun hide() {
        if (isShow) {
            ValueAnimator.ofFloat(defaultY, defaultY + height.toFloat()+bottomMargin).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    y = it.animatedValue as Float
                }
                start()
            }
            isShow = false
        }
    }

    fun show() {
        if (!isShow) {
            ValueAnimator.ofFloat(y, defaultY).apply {
                duration = 300
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    y = it.animatedValue as Float
                }
                start()
            }
            isShow = true
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && abs(event.downTime - event.eventTime) < 500) {
            for ((i, item) in items.withIndex()) {
                if (item.rect.contains(event.x, event.y)) {
                    if (i != itemActiveIndex) {
                        itemActiveIndex = i
                    } else {
                        onItemReselected?.invoke(item.id)
                        onItemReselectedListener?.onItemReselect(item.id)
                    }
                }
            }
        }

        return true
    }

    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_INDICATOR_COLOR = Color.GRAY
        private const val OPAQUE = 255
        private const val TRANSPARENT = 0
    }
}