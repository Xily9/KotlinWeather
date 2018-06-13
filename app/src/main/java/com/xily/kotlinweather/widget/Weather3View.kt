package com.xily.kotlinweather.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.xily.kotlinweather.R
import com.xily.kotlinweather.model.bean.WeatherBean
import com.xily.kotlinweather.utils.WeatherUtil
import com.xily.kotlinweather.utils.debug
import java.util.*

class Weather3View : View {
    private var mContext: Context? = null
    private val paint = Paint()
    private val path = Path()
    private val mWidth = 60
    private val oldX: Int = 0
    private val oldY: Int = 0
    private var w: Int = 0
    private var h: Int = 0
    private val mPaddingTop = 10
    private val dm = resources.displayMetrics
    private val map = WeatherUtil.weatherIcons
    private var weather3HoursDetailsInfosBeans: MutableList<WeatherBean.ValueBean.WeatherDetailsInfoBean.Weather3HoursDetailsInfosBean> = ArrayList()

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
    }

    fun setData(weather3HoursDetailsInfosBeans: List<WeatherBean.ValueBean.WeatherDetailsInfoBean.Weather3HoursDetailsInfosBean>) {
        this.weather3HoursDetailsInfosBeans.clear()
        this.weather3HoursDetailsInfosBeans.addAll(weather3HoursDetailsInfosBeans)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = dp2px((mWidth * weather3HoursDetailsInfosBeans.size).toFloat())
        h = dp2px(200f)
        setMeasuredDimension(w, h)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (weather3HoursDetailsInfosBeans.isEmpty()) return
        path.reset()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = dp2px(13f).toFloat()
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        var x = mWidth / 2
        var max: Int
        var min: Int
        min = Integer.valueOf(weather3HoursDetailsInfosBeans[0].highestTemperature)
        max = min
        for (i in 1 until weather3HoursDetailsInfosBeans.size) {
            val temp = Integer.valueOf(weather3HoursDetailsInfosBeans[i].highestTemperature)
            if (temp > max) max = temp
            if (temp < min) min = temp
        }
        val h1 = 70 / (max - min)
        val h2 = 100
        path.moveTo(dp2px(x.toFloat()).toFloat(), dp2px((h1 * (max - Integer.valueOf(weather3HoursDetailsInfosBeans[0].highestTemperature)) + mPaddingTop + 10).toFloat()).toFloat())
        for (i in weather3HoursDetailsInfosBeans.indices) {
            val value = weather3HoursDetailsInfosBeans[i]
            val temp = Integer.valueOf(value.highestTemperature)
            canvas.drawCircle(dp2px(x.toFloat()).toFloat(), dp2px((h1 * (max - temp) + mPaddingTop + 10).toFloat()).toFloat(), dp2px(3f).toFloat(), paint)
            if (i > 0) {
                path.lineTo(dp2px(x.toFloat()).toFloat(), dp2px((h1 * (max - temp) + mPaddingTop + 10).toFloat()).toFloat())
            }
            canvas.drawText(temp.toString() + "°C", dp2px(x.toFloat()).toFloat(), dp2px((h1 * (max - temp) + mPaddingTop).toFloat()).toFloat(), paint)
            canvas.drawText(value.startTime!!.substring(11, 16), dp2px(x.toFloat()).toFloat(), dp2px((h2 + 25).toFloat()).toFloat(), paint)
            val bitmap: Bitmap
            if (map.containsKey(value.img)) {
                bitmap = BitmapFactory.decodeResource(resources,
                        map.get(value.img)!!)
            } else {
                bitmap = BitmapFactory.decodeResource(resources,
                        R.drawable.weather_na)
                debug("unknown", value.weather!! + value.img!!)
            }
            val rectF = RectF(dp2px((x - 20).toFloat()).toFloat(), dp2px((h2 + 35).toFloat()).toFloat(), dp2px((x + 20).toFloat()).toFloat(), dp2px((h2 + 75).toFloat()).toFloat())
            canvas.drawBitmap(bitmap, null, rectF, paint)
            canvas.drawText(value.weather!!, dp2px(x.toFloat()).toFloat(), dp2px((h2 + 95).toFloat()).toFloat(), paint)
            x += mWidth
        }
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dp2px(2f).toFloat()
        canvas.drawPath(path, paint)
    }

    /*
    暂时还搞不定滑动冲突等各种奇葩问题,先套一个HorizontalScrollView凑活着用
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oldX = x;
                    oldY = y;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    LogUtil.d("down","down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    LogUtil.d("move","move");
                    if (Math.abs(y - oldY) < 10 && (oldX > x && getScrollX() + dm.widthPixels <= w) || (oldX < x && getScrollX() >= 0)) {
                        scrollBy(oldX - x, 0);
                        LogUtil.d("move",String.valueOf(oldX-x));
                    }else{
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    oldX = x;
                    oldY = y;
                    break;
            }
            return true;
        }
    */
    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm).toInt()
    }
}
