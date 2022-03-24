package com.paynetone.counter.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("MobileNumber")
    @Expose
    var mobileNumber: String? = null,
    @SerializedName("PasswordNew")
    @Expose
    var password: String? = null,

    @SerializedName("OTPType")
    @Expose
    var otpType: String? = null,

    @SerializedName("OTPValue")
    @Expose
    var otpValue: String? = null
)