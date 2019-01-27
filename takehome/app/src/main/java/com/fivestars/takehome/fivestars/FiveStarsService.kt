package com.fivestars.takehome.fivestars

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.fivestars.takehome.CordovaService
import com.fivestars.takehome.R

class FiveStarsService : CordovaService() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        val closeButton = contentView?.findViewById<View>(R.id.close_btn) as ImageView
        closeButton.setOnClickListener { stopSelf() }

        val chatHeadImage = contentView?.findViewById<View>(R.id.move_btn) as ImageView
        chatHeadImage.setOnTouchListener(object : View.OnTouchListener {
            private var lastAction: Int = 0
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        //remember the initial position.
                        initialX = params?.x ?: 0
                        initialY = params?.y ?: 0

                        //get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY

                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //Calculate the X and Y coordinates of the view.
                        params?.x = initialX + (event.rawX - initialTouchX).toInt()
                        params?.y = initialY + (event.rawY - initialTouchY).toInt()

                        //Update the layout with new X & Y coordinate
                        mWindowManager?.updateViewLayout(contentView, params)
                        lastAction = event.action
                        return true
                    }
                }
                return false
            }
        })
    }

    override val layoutToInflate: Int
        get() = R.layout.service_five_stars

    override val appViewParentLayoutId: Int
        get() = R.id.cordova_container
}
