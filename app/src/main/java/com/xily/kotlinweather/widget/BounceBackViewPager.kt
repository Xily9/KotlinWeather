package com.xily.kotlinweather.widget

import android.content.Context
import android.graphics.Rect
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.TranslateAnimation

class BounceBackViewPager : ViewPager {

    private var currentPosition = 0
    private val mRect = Rect()//用来记录初始位置
    private var handleDefault = true
    private var preX = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            preX = ev.x//记录起点
            currentPosition = currentItem
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> onTouchActionUp()
            MotionEvent.ACTION_MOVE -> {
                if (adapter!!.count == 1) {
                    val nowX = ev.x
                    val offset = nowX - preX
                    preX = nowX

                    if (offset > SCROLL_WIDTH) {//手指滑动的距离大于设定值
                        whetherConditionIsRight(offset)
                    } else if (offset < -SCROLL_WIDTH) {
                        whetherConditionIsRight(offset)
                    } else if (!handleDefault) {//这种情况是已经出现缓冲区域了，手指慢慢恢复的情况
                        if (left + (offset * RATIO).toInt() != mRect.left) {
                            layout(left + (offset * RATIO).toInt(), top, right + (offset * RATIO).toInt(), bottom)
                        }
                    }
                } else if (currentPosition == 0 || currentPosition == adapter!!.count - 1) {
                    val nowX = ev.x
                    val offset = nowX - preX
                    preX = nowX

                    if (currentPosition == 0) {
                        if (offset > SCROLL_WIDTH) {//手指滑动的距离大于设定值
                            whetherConditionIsRight(offset)
                        } else if (!handleDefault) {//这种情况是已经出现缓冲区域了，手指慢慢恢复的情况
                            if (left + (offset * RATIO).toInt() >= mRect.left) {
                                layout(left + (offset * RATIO).toInt(), top, right + (offset * RATIO).toInt(), bottom)
                            }
                        }
                    } else {
                        if (offset < -SCROLL_WIDTH) {
                            whetherConditionIsRight(offset)
                        } else if (!handleDefault) {
                            if (right + (offset * RATIO).toInt() <= mRect.right) {
                                layout(left + (offset * RATIO).toInt(), top, right + (offset * RATIO).toInt(), bottom)
                            }
                        }
                    }
                } else {
                    handleDefault = true
                }

                if (!handleDefault) {
                    return true
                }
            }

            else -> {
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun whetherConditionIsRight(offset: Float) {
        if (mRect.isEmpty) {
            mRect.set(left, top, right, bottom)
        }
        handleDefault = false
        layout(left + (offset * RATIO).toInt(), top, right + (offset * RATIO).toInt(), bottom)
    }

    private fun onTouchActionUp() {
        if (!mRect.isEmpty) {
            recoveryPosition()
        }
    }

    private fun recoveryPosition() {
        val ta = TranslateAnimation(left.toFloat(), mRect.left.toFloat(), 0f, 0f)
        ta.duration = 300
        startAnimation(ta)
        layout(mRect.left, mRect.top, mRect.right, mRect.bottom)
        mRect.setEmpty()
        handleDefault = true
    }

    companion object {
        private val RATIO = 0.5f//摩擦系数
        private val SCROLL_WIDTH = 10f
    }

}