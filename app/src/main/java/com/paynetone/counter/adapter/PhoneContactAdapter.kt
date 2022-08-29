package com.paynetone.counter.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ItemPhoneContactBinding
import com.paynetone.counter.model.PhoneContact
import com.paynetone.counter.utils.setSingleClick
import java.util.*
import java.util.concurrent.Executors

class PhoneContactAdapter(
    var context: Context,
    private val listContent: ArrayList<PhoneContact>,
    val itemClickListener: (item: PhoneContact) -> Unit
) : ListAdapter<PhoneContact, PhoneContactAdapter.PhoneContactHolder>(
    AsyncDifferConfig.Builder<PhoneContact>(DIFF_CALLBACK).setBackgroundThreadExecutor(
        Executors.newSingleThreadExecutor()
    ).build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneContactHolder {
        val binding = DataBindingUtil.inflate<ItemPhoneContactBinding>(
            LayoutInflater.from(context),
            R.layout.item_phone_contact,
            parent,
            false
        )
        return PhoneContactHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneContactHolder, position: Int) {
        val item = currentList[holder.adapterPosition]
        holder.binData(item)
    }

    inner class PhoneContactHolder(val binding: ItemPhoneContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binData(item: PhoneContact) {

            binding.apply {
                tvName.text = item.name
                rdColorBackground(item.name ?: " ")
                rootView.setSingleClick {
                    itemClickListener(item)
                }

            }

        }

        private fun rdColorBackground(content: String) {
            try {
                binding.imgLogo.apply {
                    text = content[0].toString()
                    setBackgroundDrawable(GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 100f
                        setColor(
                            Color.argb(
                                255,
                                Random().nextInt(256),
                                Random().nextInt(256),
                                Random().nextInt(256)
                            )
                        )
                    })

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

    }

    fun insertItem(item: PhoneContact) {
        listContent.add(item)
        submitList(listContent)

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhoneContact>() {
            override fun areItemsTheSame(oldItem: PhoneContact, newItem: PhoneContact) =
                oldItem.phone == newItem.phone

            override fun areContentsTheSame(oldItem: PhoneContact, newItem: PhoneContact) =
                oldItem == newItem
        }
    }
}