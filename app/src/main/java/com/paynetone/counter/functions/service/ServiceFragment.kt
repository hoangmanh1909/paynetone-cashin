package com.paynetone.counter.functions.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.ServiceFragmentBinding
import com.paynetone.counter.dialog.ConfirmPINCodeDialog
import com.paynetone.counter.dialog.DevelopDialog
import com.paynetone.counter.dialog.NotifyDialog
import com.paynetone.counter.dialog.PinCodeDialog
import com.paynetone.counter.functions.phone_recharge_card.PhoneRechareCardActivity
import com.paynetone.counter.functions.qr.OptionPaymentAdapter
import com.paynetone.counter.functions.qr.QRDynamicActivity
import com.paynetone.counter.model.EmployeeModel
import com.paynetone.counter.model.PaymentModel
import com.paynetone.counter.model.PaynetModel
import com.paynetone.counter.model.request.GetProviderResponse
import com.paynetone.counter.model.request.PINAddRequest
import com.paynetone.counter.model.request.TopupAddressRequest
import com.paynetone.counter.network.NetWorkController
import com.paynetone.counter.utils.*
import com.paynetone.counter.utils.ExtraConst.Companion.EXTRA_URL_TOPUP_ADDRESS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ServiceFragment : ViewFragment<ServiceContract.Presenter>(), ServiceContract.View {

    private lateinit var binding: ServiceFragmentBinding
    private lateinit var adapter: OptionPaymentAdapter
    private var paynetModel: PaynetModel? = null
    private var employeeModel: EmployeeModel?=null
    private val sharedPref by lazy { SharedPref(requireActivity()) }

    companion object {
        val instance: ServiceFragment
            get() = ServiceFragment()
    }

    override fun getLayoutId(): Int = R.layout.service_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun initView() {
        paynetModel = sharedPref.paynet
        employeeModel = sharedPref.employeeModel

    }

    fun initAdapter(providers: ArrayList<GetProviderResponse>) {
        try {
            adapter = OptionPaymentAdapter(
                requireContext(), object : OptionPaymentAdapter.OnClickItemListener {
                    override fun onClickItem(item: GetProviderResponse) {
                        if (item.isActive == Constants.PROVIDER_ACTIVE) {
                            if (sharedPref.isExistPINCode){
                                ConfirmPINCodeDialog { pinCode ->
                                    val empId = SharedPref.getInstance(requireContext()).employeeModel.id
                                    val mobileNumber = SharedPref.getInstance(requireContext()).employeeModel.mobileNumber
                                    mPresenter.requestVerifyPinCode(PINAddRequest(empId, pinCode, mobileNumber = mobileNumber))

                                }.show(childFragmentManager, "ServiceFragment")
                            }else{
                                NotifyDialog(getString(R.string.str_message_not_setting_pin_code))
                                    .show(childFragmentManager,"ServiceFragment")
                            }
//
                        } else {
                            DevelopDialog(requireContext()).show(
                                childFragmentManager,
                                "ServiceFragment"
                            )
                        }


                    }
                },
                OptionPaymentAdapter.ProviderEnum.SERVICE,
                providers
            )
            binding.recycleView.adapter = adapter
            binding.recycleView.addItemDecoration(MarginDecoration(10, 4))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun requestTopUpAddressSuccess(url: String) {
        val intent = Intent(requireActivity(), PhoneRechareCardActivity::class.java)
        intent.putExtra(EXTRA_URL_TOPUP_ADDRESS, url)
        startActivity(intent)
    }

    override fun requestVerifyPINCodeSuccess() {
        paynetModel?.let {
            mPresenter.topUpAddress(TopupAddressRequest(it.code))
        }
    }

    override fun requestError(message: String) {
        showErrorDialog(message, childFragmentManager)
    }


}