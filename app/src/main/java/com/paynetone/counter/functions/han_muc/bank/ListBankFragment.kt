package com.paynetone.counter.functions.han_muc.bank

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.paynetone.counter.R
import com.paynetone.counter.adapter.ListBankAdapter
import com.paynetone.counter.databinding.ListBankFragmentBinding
import com.paynetone.counter.utils.ExtraConst
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.handlerCopyText
import com.paynetone.counter.utils.setSingleClick


class ListBankFragment: ViewFragment<ListBankContract.Presenter>(), ListBankContract.View {

    private lateinit var binding: ListBankFragmentBinding
    private var mCode:String?=null

    private val adapter by lazy { ListBankAdapter(requireContext()){

    } }

    companion object {
        val instance : ListBankFragment
            get() = ListBankFragment()
    }

    override fun getLayoutId(): Int = R.layout.list_bank_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCode = requireActivity().intent.getStringExtra(ExtraConst.EXTRA_CODE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,container,false)
        binding.lifecycleOwner=this
        initView()
        return binding.root
    }

    private fun initView(){
        binding.apply {
            if (mCode==null){
                tvNameGuide.text = "NAPHM <SDT>"
            }else{
                tvNameGuide.text = "NAPHM ${mCode}"
            }


            ivBack.setSingleClick {
                activity?.finish()
            }
            tvNameGuide.setOnLongClickListener{
                tvNameGuide.handlerCopyText(tvNameGuide.text.toString(),requireContext())
                return@setOnLongClickListener true
            }

            imgCopy.setSingleClick {
                tvNameGuide.handlerCopyText(tvNameGuide.text.toString(),requireContext())
            }

        }
        initAdapter()
    }
    private fun initAdapter(){
        binding.recycleView.adapter = adapter
        adapter.submitList()
    }


}