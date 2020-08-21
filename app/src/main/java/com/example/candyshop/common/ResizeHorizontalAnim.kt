package com.example.candyshop.common

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import java.time.Duration

class ResizeAnimation(
    private val mView: View,
    private val mFromWidth: Float,
    private val mFromHeight: Float,
    private val mToWidth: Float,
    private val mToHeight: Float,
    private val _duration: Long
) :
    Animation() {
    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation
    ) {
        val height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight
        val width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth
        val p: ViewGroup.LayoutParams = mView.layoutParams
        p.height = height.toInt()
        p.width = width.toInt()
        mView.requestLayout()
    }

    init {
        duration = _duration
    }
}