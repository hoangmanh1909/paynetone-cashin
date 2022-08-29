package com.paynetone.counter.dialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.paynetone.counter.adapter.ManagerHanMucAdapter
import com.paynetone.counter.databinding.ManagerHanMucDialogBinding
import com.paynetone.counter.enumClass.StateNotify
import com.paynetone.counter.model.PaynetGetBalanceByIdResponse
import com.paynetone.counter.model.request.BaseRequest
import com.paynetone.counter.network.NetWorkController
import com.paynetone.counter.observer.DisplayElement
import com.paynetone.counter.observer.Observer
import com.paynetone.counter.observer.StateNotifyData
import com.paynetone.counter.utils.SharedPref
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.setSingleClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ManagerHanMucDialog(val mContext: Context,
                          val listContent:ArrayList<PaynetGetBalanceByIdResponse>,
                          private val amountOutWard:Long) : BaseDialogFragment<ManagerHanMucDialogBinding>(), DisplayElement<Any>, Observer<Any> {


    private val adapter by lazy { ManagerHanMucAdapter(mContext,listContent){ item,amount ->
        NapHanMucDialog(requireContext(), amount, item.code,amountOutWard,item).show(childFragmentManager, "ManagerHanMucDialog")
    } }

    override fun initView() {
        binding.apply {
            ivBack.setSingleClick {
                dismiss()
            }
            recycleView.apply {
                adapter = this@ManagerHanMucDialog.adapter
            }
        }

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ManagerHanMucDialogBinding.inflate(inflater,container,false)

    override fun display(data: Any) {
        if (data is StateNotifyData){
            data.also {  stateNotify ->
                when(stateNotify.state){
                    StateNotify.I_NEED_UPDATE ->{
                        SharedPref.getInstance(mContext).employeeModel.paynetID?.let {
                            requestPaynetGetBalanceByID(it)
                        }
                    }
                    else->{}
                }
            }
        }

    }

    override fun update(data: Any) {
        display(data)
    }

    override fun onStart() {
        super.onStart()
        StateNotifyData.registerObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        StateNotifyData.removeObserver(this)
    }
    private fun requestPaynetGetBalanceByID(requestId:Int):Disposable{
        val baseRequest = BaseRequest()
        baseRequest.id = requestId
        return NetWorkController.paynetGetBalanceByID(baseRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                hideProgressDialog()
                if (result.errorCode == "00"){
                    val responses = NetWorkController.getGson().fromJson<List<PaynetGetBalanceByIdResponse>>(
                                result.data, object : TypeToken<List<PaynetGetBalanceByIdResponse?>?>() {}.type)
                    adapter.updateData(responses)
                }else{
                    Toast.showToast(requireContext(),result.message)
                }
            }) { throwable ->
                throwable.printStackTrace()
                hideProgressDialog()
            }
    }

}