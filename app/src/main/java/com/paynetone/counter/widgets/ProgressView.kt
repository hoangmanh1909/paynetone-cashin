package com.paynetone.counter.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.appcompat.widget.AppCompatImageView
import com.paynetone.counter.R
import kotlin.math.floor

class ProgressView : AppCompatImageView {
    constructor(context: Context?, attrs: AttributeSet, defStyle: Int) : super(context!!, attrs, defStyle) {
        setAnimation(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context!!, attrs) {
        setAnimation(attrs)
    }

    constructor(context: Context?) : super(context!!) {}

    private fun setAnimation(attrs: AttributeSet) {
        val a: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView)
        val frameCount = a.getInt(R.styleable.ProgressView_frameCount, 8)
        val duration = a.getInt(R.styleable.ProgressView_duration, 800)
        a.recycle()
        setAnimation(frameCount, duration)
    }

    fun setAnimation(frameCount: Int, duration: Int) {
        val a = AnimationUtils.loadAnimation(context,R.anim.anim_loading)
        a.duration = duration.toLong()
        a.interpolator = Interpolator { input -> floor((input * frameCount).toDouble()).toFloat() / frameCount }
        startAnimation(a)
    }
}