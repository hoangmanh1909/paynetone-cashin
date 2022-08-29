package com.paynetone.counter.functions.qr

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ItemGroupBankBinding
import com.paynetone.counter.databinding.ItemOptionPaymentBinding
import com.paynetone.counter.databinding.ItemServiceBinding
import com.paynetone.counter.model.PaymentModel
import com.paynetone.counter.model.ServiceModel
import com.paynetone.counter.model.request.GetProviderResponse
import com.paynetone.counter.utils.Constants
import com.paynetone.counter.utils.Constants.*
import com.paynetone.counter.utils.loadImageWithGlide
import com.paynetone.counter.utils.loadImageWithGlideResource
import com.paynetone.counter.utils.setSingleClick

class OptionPaymentAdapter(private val mContext:Context,
                           var listener: OnClickItemListener,
                           private var typeProvider:ProviderEnum,
                           private val listProvider : ArrayList<GetProviderResponse>
                           ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
    companion object{
        const val TYPE_QR = 1
        const val TYPE_SERVICE = 2
        const val TYPE_GROUP_BANK = 3
    }

    fun initItemGroup(){
        try {
            val itemGroup = GetProviderResponse(type = TYPE_BANK, name = "Xem tất cả", isActive = "Y", itemGroup = true)
            val images = arrayListOf<String>()
            for (item in listProvider){
                if (images.size > 4 ) break
                if (item.type == TYPE_BANK) images.add(item.icon ?: "")
            }
            itemGroup.imageGroup = images
            listProvider.add(itemGroup)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private val listContent by lazy {
        when(typeProvider){
            ProviderEnum.BANK -> { arrayListOf<GetProviderResponse>().apply {
                    addAll( listProvider.filter { it.type == TYPE_BANK }.take(7))
                    add(GetProviderResponse(type = TYPE_BANK,name = "Xem tất cả", isActive = "Y", itemGroup = true))
                }
            }
            ProviderEnum.E_WALLET -> listProvider.filter { it.type == TYPE_EWALLET }
            ProviderEnum.PAYMENT_QR -> listProvider.filter { it.type == TYPE_PAYMENT_QR }
            else -> listProvider.filter { it.type == TYPE_GTGT }
        }
    }



    interface OnClickItemListener {
        fun onClickItem(item: GetProviderResponse)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_SERVICE -> {
                val binding = DataBindingUtil.inflate<ItemServiceBinding>(LayoutInflater.from(mContext), R.layout.item_service, parent, false)
                ServiceHolder(binding)
            }
            else->{
                val binding = DataBindingUtil.inflate<ItemOptionPaymentBinding>(LayoutInflater.from(mContext), R.layout.item_option_payment, parent, false)
                OptionPaymentHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ServiceHolder){
            holder.apply {
                this.binData(listContent[position])
            }
        }
        if (holder is OptionPaymentHolder){
            holder.apply {
                this.binData(listContent[position])
            }
        }
    }

    override fun getItemCount(): Int = listContent.size

    override fun getItemViewType(position: Int): Int {
        return when(listContent[position].type){
            TYPE_GTGT -> TYPE_SERVICE
            else ->  TYPE_QR
        }
    }

    inner class OptionPaymentHolder(private val binding:ItemOptionPaymentBinding) : RecyclerView.ViewHolder(binding.root){

        fun binData(item:GetProviderResponse){
            binding.apply {
                if (item.paymentType ==  Constants.PAYMENT_TYPE_VIETTEL){
                    tvName.text = item.name?.replace(" ","\n")
                }else{
                    tvName.text = item.name
                }

                if (item.itemGroup)
                    imgLogo.loadImageWithGlideResource(ContextCompat.getDrawable(mContext,R.drawable.ic_group_bank))
                else
                    imgLogo.loadImageWithGlide(item.icon ?: "")

                rootView.setSingleClick {
                    listener.onClickItem(item)
                }
            }
        }
    }

    inner class ServiceHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root){

        fun binData(item: GetProviderResponse){
            binding.apply {
                tvName.text = item.name
                imgLogo.loadImageWithGlide(item.icon ?: "")
                rootView.setSingleClick {
                    listener.onClickItem(item)
                }
            }

        }

    }
//    inner class GroupBankHolder(val binding:ItemGroupBankBinding):RecyclerView.ViewHolder(binding.rootView){
//        fun binData(item: GetProviderResponse){
//            binding.apply {
//                try {
//                    imgLogo1.loadImageWithGlide(item.imageGroup[0])
//                    imgLogo2.loadImageWithGlide(item.imageGroup[1])
//                    imgLogo3.loadImageWithGlide(item.imageGroup[2])
//                    imgLogo4.loadImageWithGlide(item.imageGroup[3])
//                    rootView.setSingleClick {
//                        listener.onClickItem(item)
//                    }
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//
//            }
//
//        }
//    }
    enum class ProviderEnum {
        BANK,
        E_WALLET,
        PAYMENT_QR,
        SERVICE
    }



}