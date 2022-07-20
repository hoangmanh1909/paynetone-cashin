package com.paynetone.counter.functions.qr

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ItemOptionPaymentBinding
import com.paynetone.counter.model.PaymentModel
import com.paynetone.counter.utils.Constants.*
import com.paynetone.counter.utils.setSingleClick

class OptionPaymentAdapter(private val mContext:Context,var listener: OnClickItemListener) : RecyclerView.Adapter<OptionPaymentAdapter.OptionPaymentHolder>() {

    private val listContent by lazy {
        arrayListOf<PaymentModel>(PaymentModel(PAYMENT_VIETTEL_PAY,mContext.resources.getString(R.string.str_viettel_money), R.drawable.viettel_money,true),
            PaymentModel(PAYMENT_ZALO_PAY,mContext.resources.getString(R.string.str_zalo_pay),R.drawable.zalopay),
            PaymentModel(PAYMENT_SHOPPE_PAY,mContext.resources.getString(R.string.str_shoppe_pay),R.drawable.ic_shoppe_pay),
            PaymentModel(PAYMENT_VN_PAY,mContext.resources.getString(R.string.str_vn_pay),R.drawable.ic_vnpay),
            PaymentModel(PAYMENT_MOCA,mContext.resources.getString(R.string.str_moca),R.drawable.ic_moca),
            PaymentModel(PAYMENT_VIETQR,mContext.resources.getString(R.string.str_viet_qr),R.drawable.ic_viet_qr)
        )
    }

    interface OnClickItemListener {
        fun onClickItem(item: PaymentModel)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionPaymentHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOptionPaymentBinding.inflate(layoutInflater, parent, false)
        return OptionPaymentHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionPaymentHolder, position: Int) {
        holder.binData(listContent[position],position)

    }

    override fun getItemCount(): Int = listContent.size

    inner class OptionPaymentHolder(private val binding:ItemOptionPaymentBinding) : RecyclerView.ViewHolder(binding.root){

        fun binData(item:PaymentModel,position: Int){
            binding.apply {
                tvName.text = item.name
                imgLogo.setImageResource(item.imageLogo)

                if (item.isChecked) imgCheck.visibility = View.VISIBLE
                else imgCheck.visibility = View.GONE

                rootView.setSingleClick {
                    listener.onClickItem(item)
                    if (!item.isChecked) {
                        item.isChecked=true
                        resetSelected(position)
                    }
                }
            }
        }
    }

    fun resetSelected(position: Int) {
        listContent.let {
            notifyItemChanged(position)
            for (i in it.indices) {
                if (i != position && it[i].isChecked) {
                    it[i].isChecked=false
                    notifyItemChanged(i)
                }
            }

        }

    }

}