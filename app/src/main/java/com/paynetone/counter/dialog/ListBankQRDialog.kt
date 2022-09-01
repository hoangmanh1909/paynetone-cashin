package com.paynetone.counter.dialog

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paynetone.counter.R
import com.paynetone.counter.adapter.ListBankQrAdapter
import com.paynetone.counter.databinding.DialogListBankBinding
import com.paynetone.counter.functions.qr.QRDynamicActivity
import com.paynetone.counter.model.request.GetProviderResponse
import com.paynetone.counter.utils.*

class ListBankQRDialog(private val listProvider : ArrayList<GetProviderResponse>) : BaseDialogFragment<DialogListBankBinding>() {

    private val adapter by lazy { ListBankQrAdapter(requireContext(),object : ListBankQrAdapter.OnClickItemListener{
        override fun onClickItem(item: GetProviderResponse) {
            if (item.isActive==Constants.PROVIDER_ACTIVE){
                val intent = Intent(requireActivity(), QRDynamicActivity::class.java)
                intent.putExtra(ExtraConst.EXTRA_PROVIDER_RESPONSE, item)
                startActivity(intent)
            }else{
                DevelopDialog(requireContext()).show(childFragmentManager, "HomeFragment")
            }

        }

    },listProvider) }

    override fun initView() {
        binding.apply {
            ivBack.setSingleClick {
                dismiss()
            }
            rootView.setSingleClick {
                it.hideKeyboard()
            }
        }
        initAdapter()
        listenerSearchView()

    }
    private fun initAdapter(){
        binding.recycleView.apply {
            adapter = this@ListBankQRDialog.adapter
            addItemDecoration(MarginDecoration(20,4))
        }

    }

    private fun listenerSearchView(){
        binding.edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s.toString().isNotBlank()) this@ListBankQRDialog.adapter.filter.filter(s)
               else{
                   Handler(Looper.myLooper()!!).postDelayed({
                       this@ListBankQRDialog.adapter.notifyAllData()
                   },200L)
               }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun initStyle(): Int = R.style.FullScreenDialogListBankQR

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogListBankBinding.inflate(inflater,container,false)

}