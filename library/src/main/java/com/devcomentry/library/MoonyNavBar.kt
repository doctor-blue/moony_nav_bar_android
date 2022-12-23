package com.devcomentry.library

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope
import androidx.annotation.XmlRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import com.devcomentry.library.*
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * Create by Nguyen Van Tan 7/2020
 * */
@RestrictTo(Scope.LIBRARY_GROUP)
class MoonyNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MoonyNavBarStyle
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
                items = NavBarParser(context, value).parse()
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
            applyItemActiveIndex()
        }

    var isShow: Boolean = true


    private var indicatorLocation = 0f
    private val rect = RectF()
    private var itemWidth: Float = 0f
    private var itemHeight: Float = 0f
    private var items = listOf<NavBarItem>()
    private var currentIconTint: Int = itemIconTintActive
    private val defaultY by lazy { y }
    private var bottomMargin = 0f

    @SuppressLint("RestrictedApi")
    private var menu: MenuBuilder = MenuBuilder(context)
    private var menuInflater: MenuInflater? = null


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

    @SuppressLint("RestrictedApi")
    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MoonyNavBar,
            defStyleAttr,
            0
        )
        try {

            barBackground = typedArray.getColor(
                R.styleable.MoonyNavBar_backgroundColor,
                barBackground
            )
            itemMenuRes = typedArray.getResourceId(
                R.styleable.MoonyNavBar_menu,
                itemMenuRes
            )
            inflate()

            indicatorColor =
                typedArray.getColor(R.styleable.MoonyNavBar_indicatorColor, indicatorColor)

            itemIconSize = typedArray.getDimension(
                R.styleable.MoonyNavBar_iconSize,
                itemIconSize
            )
            itemIconTintActive =
                typedArray.getColor(R.styleable.MoonyNavBar_iconTintActive, itemIconTintActive)

            itemIconTint = typedArray.getColor(R.styleable.MoonyNavBar_iconTint, itemIconTint)

            indicatorDuration =
                typedArray.getInt(R.styleable.MoonyNavBar_duration, indicatorDuration.toInt())
                    .toLong()


            val indicatorType =
                typedArray.getInt(R.styleable.MoonyNavBar_indicatorType, 0)

            if (indicatorType == 0)

                this.indicatorType = IndicatorType.LINE
            if (indicatorType == 1)
                this.indicatorType = IndicatorType.POINT
            if (indicatorType == 2)
                this.indicatorType = IndicatorType.NONE

            val indicatorPosition =
                typedArray.getInt(R.styleable.MoonyNavBar_indicatorPosition, 1)

            this.indicatorPosition =
                if (indicatorPosition == 0) IndicatorPosition.TOP else IndicatorPosition.BOTTOM

            menu.setCallback(@SuppressLint("RestrictedApi")
            object : MenuBuilder.Callback {
                override fun onMenuItemSelected(
                    menu: MenuBuilder,
                    item: MenuItem
                ): Boolean {
                    /*return if (onItemReselectedListener != null && item.itemId == items[itemActiveIndex].id) {
                       *//* this@BottomNavigationView.reselectedListener.onNavigationItemReselected(
                            item
                        )*//*
                        true
                    } else {
                        *//*this@BottomNavigationView.selectedListener != null && !this@BottomNavigationView.selectedListener.onNavigationItemSelected(
                            item
                        )*//*
                        false
                    }*/
                    return true
                }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun inflate() {
        if (menuInflater == null) {
            menuInflater = SupportMenuInflater(this.context)
        }

        menuInflater!!.inflate(itemMenuRes, menu)
    }

    fun setupWithNavController(navController: NavController) {
        NavigationHelper.setupWithNavController(this.menu, this, navController)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val margins: ViewGroup.MarginLayoutParams? =
            ViewGroup.MarginLayoutParams::class.java.cast(layoutParams)
        margins?.let { bottomMargin = it.bottomMargin.toFloat() }
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
        }

        if (indicatorType == IndicatorType.POINT) {
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

    private fun animateAlpha(item: NavBarItem, to: Int) {
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
            ValueAnimator.ofFloat(defaultY, defaultY + height.toFloat() + bottomMargin).apply {
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
                        onItemSelectedListener?.onItemSelect(item.id, i)
                        onItemSelected?.invoke(item.id)
                    } else {
                        onItemReselected?.invoke(item.id)
                        onItemReselectedListener?.onItemReselect(item.id, i)
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