package com.paynetone.counter.utils

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.setSingleClick(execution: (View) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(p0: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < Constants.THRESHOLD_CLICK_TIME) {
                return
            }
            lastClickTime = SystemClock.elapsedRealtime()
            execution.invoke(this@setSingleClick)
        }
    })
}
fun View?.hideKeyboard() {
    try {
        val manager =
            this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}