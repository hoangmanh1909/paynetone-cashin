package com.paynetone.counter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawRequest {
    @SerializedName("MerchantID")
    @Expose
    private Integer merchantID;
    @SerializedName("PaynetID")
    @Expose
    private Integer paynetID;
    @SerializedName("TransDetail")
    @Expose
    private String transDetail;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("Fee")
    @Expose
    private Integer fee;
    @SerializedName("TransAmount")
    @Expose
    private Integer transAmount;
    @SerializedName("TransReason")
    @Expose
    private String transReason;
    @SerializedName("BankID")
    @Expose
    private Integer bankID;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("AccountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("WithdrawCategory")
    @Expose
    private int withdrawCategory ;
    @SerializedName("WalletID")
    @Expose
    private int walletID ;
    @SerializedName("PosID")
    @Expose
    private String posID ;

    public String getPosID() {
        return posID;
    }

    public void setPosID(String posID) {
        this.posID = posID;
    }

    public int getWalletID() {
        return walletID;
    }

    public void setWalletID(int walletID) {
        this.walletID = walletID;
    }

    public Integer getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(Integer merchantID) {
        this.merchantID = merchantID;
    }

    public Integer getPaynetID() {
        return paynetID;
    }

    public void setPaynetID(Integer paynetID) {
        this.paynetID = paynetID;
    }

    public String getTransDetail() {
        return transDetail;
    }

    public void setTransDetail(String transDetail) {
        this.transDetail = transDetail;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Integer transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransReason() {
        return transReason;
    }

    public void setTransReason(String transReason) {
        this.transReason = transReason;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setWithdrawCategory(int withdrawCategory){
        this.withdrawCategory=withdrawCategory;
    }
    public int getWithdrawCategory(){
        return withdrawCategory;
    }
}
