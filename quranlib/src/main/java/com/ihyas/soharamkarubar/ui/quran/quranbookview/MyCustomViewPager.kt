package com.ihyas.soharamkarubar.ui.quran.quranbookview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class MyCustomViewPager(context: Context, attrs: AttributeSet?) :
    ViewPager(context, attrs) {
    var mStartDragX = 0f
    var mListener: OnSwipeOutListener? = null
    fun setOnSwipeOutListener(listener: OnSwipeOutListener?) {
        mListener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mStartDragX = x
            MotionEvent.ACTION_MOVE -> if (mStartDragX < x && currentItem == 0) {
            } else if (mStartDragX > x && currentItem == adapter!!.count - 1) {
                Log.d("state" ,"lastPage")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    interface OnSwipeOutListener {
        fun onSwipeOutAtStart()
        fun onSwipeOutAtEnd()
    }
}