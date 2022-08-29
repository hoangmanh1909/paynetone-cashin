package com.paynetone.counter.dialog

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paynetone.counter.R
import com.paynetone.counter.adapter.ListBankQrAdapter
import com.paynetone.counter.databinding.DialogListBankBinding
import com.paynetone.counter.functions.qr.QRDynamicActivity
import com.paynetone.counter.model.request.GetProviderResponse
import com.paynetone.counter.utils.ExtraConst
import com.paynetone.counter.utils.MarginDecoration
import com.paynetone.counter.utils.hideKeyboard
import com.paynetone.counter.utils.setSingleClick

class ListBankQRDialog(private val listProvider : ArrayList<GetProviderResponse>) : BaseDialogFragment<DialogListBankBinding>() {

    private val adapter by lazy { ListBankQrAdapter(requireContext(),object : ListBankQrAdapter.OnClickItemListener{
        override fun onClickItem(item: GetProviderResponse) {
            val intent = Intent(requireActivity(), QRDynamicActivity::class.java)
            intent.putExtra(ExtraConst.EXTRA_PROVIDER_RESPONSE, item)
            startActivity(intent)
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
                this@ListBankQRDialog.adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun initStyle(): Int = R.style.FullScreenDialogListBankQR

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) = DialogListBankBinding.inflate(inflater,container,false)

}