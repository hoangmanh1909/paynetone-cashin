package com.paynetone.counter.model

import androidx.annotation.DrawableRes

data class PaymentModel(
    val id:Int,
    val name:String,
    @DrawableRes
    val imageLogo: Int,
    var isChecked : Boolean=false
)