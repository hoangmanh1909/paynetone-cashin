package com.paynetone.counter.functions.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ItemOptionDashboardBinding
import com.paynetone.counter.databinding.ItemPhoneContactBinding
import com.paynetone.counter.model.response.WalletResponse
import com.paynetone.counter.utils.setSingleClick

class DashboardOptionAdapter(val mContext:Context,val itemClickListener: (dashboardModel: DashboardModel) -> Unit) : RecyclerView.Adapter<DashboardOptionAdapter.DashboardViewHolder>() {

    private val listContent by lazy { arrayListOf(DashboardModel(0,mContext.resources.getString(R.string.str_dashboard_day)),
        DashboardModel(1,mContext.resources.getString(R.string.str_dashboard_week)),
        DashboardModel(2,mContext.resources.getString(R.string.str_dashboard_month)),
        DashboardModel(3,mContext.resources.getString(R.string.str_dashboard_quarter)),) }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = DataBindingUtil.inflate<ItemOptionDashboardBinding>(LayoutInflater.from(mContext), R.layout.item_option_dashboard, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.binData(listContent[holder.bindingAdapterPosition])
    }
    inner class DashboardViewHolder(val binding:ItemOptionDashboardBinding) : RecyclerView.ViewHolder(binding.root){

        fun binData(item:DashboardModel){
            binding.apply {
                tvName.text = item.name
                rootView.setSingleClick {
                    itemClickListener(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = listContent.size
}