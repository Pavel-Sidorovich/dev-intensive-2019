package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private val DEFAULT_BORDER_WIDTH = 2//.dpToPixels
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    private val paint: Paint = Paint().apply { isAntiAlias = true }
    private val paintBorder: Paint = Paint().apply { isAntiAlias = true }
    private val paintBackground: Paint = Paint().apply { isAntiAlias = true }
    private var circleCenter: Int = 0
    private var heightCircle: Int = 0

    private var borderWidth: Int = Utils.dpToPx(DEFAULT_BORDER_WIDTH, context)
    private var borderColor = DEFAULT_BORDER_COLOR

    private var civImage: Bitmap? = null
    private var civDrawable: Drawable? = null

    fun getBorderWidth() = Utils.pxToDp(borderWidth, context)//.pxToDimensionPixels

    fun setBorderWidth(dp: Int) {
        if (dp == borderWidth) return
        borderWidth = Utils.dpToPx(dp, context)
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        val color = Color.parseColor(hex)
        if(color == borderColor) return
        borderColor = color
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        if(colorId == borderColor) return
        borderColor = ContextCompat.getColor(App.applicationContext(), colorId)
    }

    init {
        if(attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)

            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, Utils.dpToPx(DEFAULT_BORDER_WIDTH, context))
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)

            a.recycle()
        }
    }

    override fun getScaleType(): ScaleType =
        super.getScaleType().let { if (it == null || it != CENTER_INSIDE) CENTER_CROP else it }

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType != CENTER_CROP && scaleType != CENTER_INSIDE) {
            throw IllegalArgumentException(String.format("ScaleType is not supported." , scaleType))
        } else {
            super.setScaleType(scaleType)
        }
    }

    override fun onDraw(canvas: Canvas) {
        loadBitmap()

        if (civImage == null) return

        val circleCenterWithBorder = circleCenter + borderWidth
        val margeRadius = 0f

        canvas.drawCircle(circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), circleCenterWithBorder - margeRadius, paintBorder)
        canvas.drawCircle(circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), circleCenter - margeRadius, paintBackground)
        canvas.drawCircle(circleCenterWithBorder.toFloat(), circleCenterWithBorder.toFloat(), circleCenter - margeRadius, paint)
    }

    private fun update() {
        if (civImage != null)
            updateShader()

        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)

        heightCircle = Math.min(usableWidth, usableHeight)

        circleCenter = ((heightCircle - borderWidth * 2) / 2)
        paintBorder.color = borderColor

        invalidate()
    }

    private fun loadBitmap() {
        if (civDrawable == drawable) return

        civDrawable = drawable
        civImage = drawableToBitmap(civDrawable)
        updateShader()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    private fun updateShader() {
        civImage?.also {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val scale: Float
            val dx: Float
            val dy: Float

            when (scaleType) {
                CENTER_CROP -> if (it.width * height > width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                CENTER_INSIDE -> if (it.width * height < width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                else -> {
                    scale = 0f
                    dx = 0f
                    dy = 0f
                }
            }

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })

            paint.shader = shader
        }
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? =
        when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> try {
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measure(widthMeasureSpec)
        val height = measure(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measure(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> specSize
            else -> heightCircle
        }
    }
}

///*
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package ru.skillbranch.devintensive.ui.custom
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.*
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Build
//import android.util.AttributeSet
//import android.util.Log
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewOutlineProvider
//import android.widget.ImageView
//import androidx.annotation.*
//import ru.skillbranch.devintensive.R
//import kotlin.math.min
//import kotlin.math.pow
//import android.util.TypedValue
//import ru.skillbranch.devintensive.utils.Utils.dpToPx
//import ru.skillbranch.devintensive.utils.Utils.pxToDp
//
//
//class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) :
//    ImageView(context, attrs, defStyle) {
//
//    companion object {
//
//        private val SCALE_TYPE = ScaleType.CENTER_CROP
//
//        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
//        private const val COLOR_DRAWABLE_DIMENSION = 2
//
//        private const val DEFAULT_BORDER_WIDTH = 2
//        private const val DEFAULT_BORDER_COLOR = Color.WHITE
//        private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.GREEN
//        private const val DEFAULT_BORDER_OVERLAY = false
//    }
//
//    private val mDrawableRect = RectF()
//    private val mBorderRect = RectF()
//
//    private val mShaderMatrix = Matrix()
//    private val mBitmapPaint = Paint()
//    private val mBorderPaint = Paint()
//    private val mCircleBackgroundPaint = Paint()
//
//    private var mBorderColor = DEFAULT_BORDER_COLOR
//    private var mBorderWidth = DEFAULT_BORDER_WIDTH
//    private var mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR
//
//    private var mBitmap: Bitmap? = null
//    private var mBitmapShader: BitmapShader? = null
//    private var mBitmapWidth: Int = 0
//    private var mBitmapHeight: Int = 0
//
//    private var mDrawableRadius: Float = 0.toFloat()
//    private var mBorderRadius: Float = 0.toFloat()
//
//    private var mColorFilter: ColorFilter? = null
//
//    private var mReady: Boolean = false
//    private var mSetupPending: Boolean = false
//    private var mBorderOverlay: Boolean = false
//
//    var isDisableCircularTransformation: Boolean = false
//        set(disableCircularTransformation) {
//            if (isDisableCircularTransformation == disableCircularTransformation) {
//                return
//            }
//
//            field = disableCircularTransformation
//            initializeBitmap()
//        }
//
//    fun getBorderColor() = mBorderColor
//
//    fun setBorderColor(hex: String) {
//        val borderColor = Color.parseColor(hex)
//        if (borderColor == mBorderColor) {
//            return
//        }
//
//        mBorderColor = borderColor
//        mBorderPaint.color = mBorderColor
//        invalidate()
//    }
//
//    fun setBorderColor(@ColorRes colorId: Int) {
//        if (colorId == mBorderColor) {
//            return
//        }
//
////        mBorderColor = resources.getColor(colorId, context.theme)
//
//        mBorderPaint.color = ContextCompat.getColor(context, colorId)
//        invalidate()
//    }
//
//    fun setBorderWidth(@Dimension dp:Int){
//        if (dp == dpToPx(mBorderWidth.toFloat(), context)) {
//            return
//        }
//
//        mBorderWidth = dpToPx(mBorderWidth.toFloat(),context)
//        setup()
//    }
//
//    fun getBorderWidth():Int = pxToDp(mBorderColor.toFloat(), context)
//
//    init {
//        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0)
//
//        mBorderWidth = a.getDimensionPixelSize(
//            R.styleable.CircleImageView_cv_borderWidth,
//            dpToPx(DEFAULT_BORDER_WIDTH.toFloat(), context)
//        )
//        println(mBorderWidth)
//        mBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
//        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_cv_borderOverlay, DEFAULT_BORDER_OVERLAY)
//        mCircleBackgroundColor =
//            a.getColor(R.styleable.CircleImageView_cv_circleBackgroundColor, DEFAULT_CIRCLE_BACKGROUND_COLOR)
//
//        a.recycle()
//
//        init()
//    }
//
//    private var circleBackgroundColor: Int
//        get() = mCircleBackgroundColor
//        set(@ColorInt circleBackgroundColor) {
//            if (circleBackgroundColor == mCircleBackgroundColor) {
//                return
//            }
//
//            mCircleBackgroundColor = circleBackgroundColor
//            mCircleBackgroundPaint.color = circleBackgroundColor
//            invalidate()
//        }
//
//    var isBorderOverlay: Boolean
//        get() = mBorderOverlay
//        set(borderOverlay) {
//            if (borderOverlay == mBorderOverlay) {
//                return
//            }
//
//            mBorderOverlay = borderOverlay
//            setup()
//        }
//
//    private fun init() {
//        super.setScaleType(SCALE_TYPE)
//        mReady = true
//
//
//        outlineProvider = OutlineProvider()
//
//
//        if (mSetupPending) {
//            setup()
//            mSetupPending = false
//        }
//    }
//
//    override fun getScaleType(): ImageView.ScaleType {
//        return SCALE_TYPE
//    }
//
//    override fun setScaleType(scaleType: ImageView.ScaleType) {
//        if (scaleType != SCALE_TYPE) {
//            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType))
//        }
//    }
//
//    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
//        if (adjustViewBounds) {
//            throw IllegalArgumentException("adjustViewBounds not supported.")
//        }
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        if (isDisableCircularTransformation) {
//            super.onDraw(canvas)
//            return
//        }
//
//        if (mBitmap == null) {
//            return
//        }
//
//        Log.d("M_Circle", "onDrawCircle")
//
//        if (mCircleBackgroundColor != Color.TRANSPARENT) {
//            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mCircleBackgroundPaint)
//        }
//        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
//        if (mBorderWidth > 0) {
//            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), mBorderRadius, mBorderPaint)
//        }
//
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        setup()
//    }
//
//    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
//        super.setPadding(left, top, right, bottom)
//        setup()
//    }
//
//    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
//        super.setPaddingRelative(start, top, end, bottom)
//        setup()
//    }
//
//    override fun setImageBitmap(bm: Bitmap) {
//        super.setImageBitmap(bm)
//        initializeBitmap()
//    }
//
//    override fun setImageDrawable(drawable: Drawable?) {
//        super.setImageDrawable(drawable)
//        initializeBitmap()
//    }
//
//    override fun setImageResource(@DrawableRes resId: Int) {
//        super.setImageResource(resId)
//        initializeBitmap()
//    }
//
//    override fun setImageURI(uri: Uri?) {
//        super.setImageURI(uri)
//        initializeBitmap()
//    }
//
//    override fun setColorFilter(cf: ColorFilter) {
//        if (cf === mColorFilter) {
//            return
//        }
//
//        mColorFilter = cf
//        applyColorFilter()
//        invalidate()
//    }
//
//    override fun getColorFilter(): ColorFilter? {
//        return mColorFilter
//    }
//
//    private fun applyColorFilter() {
//        mBitmapPaint.colorFilter = mColorFilter
//    }
//
//    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
//        if (drawable == null) {
//            return null
//        }
//
//        if (drawable is BitmapDrawable) {
//            return drawable.bitmap
//        }
//
//        return try {
//            val bitmap: Bitmap = if (drawable is ColorDrawable) {
//                Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
//            } else {
//                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
//            }
//
//            val canvas = Canvas(bitmap)
//            drawable.setBounds(0, 0, canvas.width, canvas.height)
//            drawable.draw(canvas)
//            bitmap
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//
//    }
//
//    private fun initializeBitmap() {
//        if (isDisableCircularTransformation) {
//            mBitmap = null
//        } else {
//            mBitmap = getBitmapFromDrawable(drawable)
//        }
//        setup()
//    }
//
//    private fun setup() {
//        if (!mReady) {
//            mSetupPending = true
//            return
//        }
//
//        if (width == 0 && height == 0) {
//            return
//        }
//
//        if (mBitmap == null) {
//            invalidate()
//            return
//        }
//
//        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//
//        mBitmapPaint.isAntiAlias = true
//        mBitmapPaint.shader = mBitmapShader
//
//        mBorderPaint.style = Paint.Style.STROKE
//        mBorderPaint.isAntiAlias = true
//        mBorderPaint.color = mBorderColor
//        mBorderPaint.strokeWidth = mBorderWidth.toFloat()
//
//        mCircleBackgroundPaint.style = Paint.Style.FILL
//        mCircleBackgroundPaint.isAntiAlias = true
//        mCircleBackgroundPaint.color = mCircleBackgroundColor
//
//        mBitmapHeight = mBitmap!!.height
//        mBitmapWidth = mBitmap!!.width
//
//        mBorderRect.set(calculateBounds())
//        mBorderRadius = min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f)
//
//        mDrawableRect.set(mBorderRect)
//        if (!mBorderOverlay && mBorderWidth > 0) {
//            mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f)
//        }
//        mDrawableRadius = min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)
//
//        applyColorFilter()
//        updateShaderMatrix()
//        invalidate()
//    }
//
//    private fun calculateBounds(): RectF {
//        val availableWidth = width - paddingLeft - paddingRight
//        val availableHeight = height - paddingTop - paddingBottom
//
//        val sideLength = min(availableWidth, availableHeight)
//
//        val left = paddingLeft + (availableWidth - sideLength) / 2f
//        val top = paddingTop + (availableHeight - sideLength) / 2f
//
//        return RectF(left, top, left + sideLength, top + sideLength)
//    }
//
//    private fun updateShaderMatrix() {
//        val scale: Float
//        var dx = 0f
//        var dy = 0f
//
//        mShaderMatrix.set(null)
//
//        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
//            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
//            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
//        } else {
//            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
//            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
//        }
//
//        mShaderMatrix.setScale(scale, scale)
//        mShaderMatrix.postTranslate((dx + 0.5f).toInt() + mDrawableRect.left, (dy + 0.5f).toInt() + mDrawableRect.top)
//
//        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
//    }
//
//    private fun inTouchableArea(x: Float, y: Float): Boolean {
//        return (x - mBorderRect.centerX()).toDouble().pow(2.0) + (y - mBorderRect.centerY()).toDouble().pow(2.0) <= mBorderRadius.toDouble().pow(
//            2.0
//        )
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private inner class OutlineProvider : ViewOutlineProvider() {
//
//        override fun getOutline(view: View, outline: Outline) {
//            val bounds = Rect()
//            mBorderRect.roundOut(bounds)
//            outline.setRoundRect(bounds, bounds.width() / 2.0f)
//        }
//
//    }
//
//}
