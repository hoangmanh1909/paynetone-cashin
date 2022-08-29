package com.paynetone.counter.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.paynetone.counter.R

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
fun AppCompatEditText.passwordToggle(context: Context,imageView: AppCompatImageView){
    if (this.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
        this.transformationMethod = HideReturnsTransformationMethod.getInstance()
        imageView.setImageDrawable(
            ContextCompat.getDrawable(context,
            R.drawable.ic_show_password))
    } else {
        this.transformationMethod = PasswordTransformationMethod.getInstance()
        imageView.setImageDrawable(
            ContextCompat.getDrawable(context,
            R.drawable.ic_hide_password))
    }
    this.setSelection(this.length())
}
fun AppCompatTextView.handlerCopyText(text :String,context: Context){
    val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip: ClipData = ClipData.newPlainText("note_copy", text)
    myClipboard.setPrimaryClip(myClip)
    Toast.showToast(context,"Sao chép thành công!")
}

fun ImageView.loadImageWithGlide(url:String){
    Glide.with(this)
        .load(Utils.getUrlImage(url))
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}
fun ImageView.loadImageWithGlideResource(drawable:Drawable?){
    Glide.with(this)
        .load(drawable)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
}
fun ImageView.loadImageWithGlideUri(uri:Uri){
    Glide.with(this)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .apply(RequestOptions().centerCrop())
        .into(this)
}

fun AppCompatEditText.disableCopyPaste() {
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
        override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?) = false

        override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?) = false

        override fun onActionItemClicked(mode: android.view.ActionMode?, item: MenuItem?)= false

        override fun onDestroyActionMode(mode: android.view.ActionMode?) {}
    }
    //disable action mode when edittext gain focus at first
    customInsertionActionModeCallback = object : android.view.ActionMode.Callback {
        override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?)= false

        override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?)= false

        override fun onActionItemClicked(mode: android.view.ActionMode?, item: MenuItem?)= false

        override fun onDestroyActionMode(mode: android.view.ActionMode?) {}
    }
}

fun View.resizeLayout(w: Int, h: Int) {
    try {
        layoutParams.apply {
            width = w
            height = h
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.resizeWidth(w: Int) {
    try {
        layoutParams.apply {
            width = w
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.resizeWidthWithHeightMatchParent(w: Int) {
    try {
        layoutParams.apply {
            width = w
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.resizeHeight(h: Int) {
    try {
        layoutParams.apply {
            height = h
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.resizeLayout(size: ViewSize) {
    try {
        layoutParams.apply {
            width = size.width.toInt()
            height = size.height.toInt()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}