package com.paynetone.counter.functions.phone_recharge_card

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.paynetone.counter.R
import com.paynetone.counter.databinding.PhoneRechargeCardFragmentBinding
import com.paynetone.counter.dialog.ErrorDialog
import com.paynetone.counter.dialog.PhoneContactDialog
import com.paynetone.counter.utils.Toast
import com.paynetone.counter.utils.setSingleClick


class PhoneRechareCardFragment : ViewFragment<PhoneRechareCardContract.Presenter>(), PhoneRechareCardContract.View {

    private lateinit var binding: PhoneRechargeCardFragmentBinding
    private var hotline :String? = null
    private var url :String? = null

    companion object {
        val instance: PhoneRechareCardFragment
            get() = PhoneRechareCardFragment()
    }

    override fun getLayoutId(): Int = R.layout.phone_recharge_card_fragment

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
        initWebView()
        this.url = mPresenter.urlTopUpAddress()
        binding.apply {
            ivBack.setSingleClick {
                if (webview.canGoBack()) {
                    webview.goBack()
                } else {
                    activity?.finish()
                }

                url?.let {
                    if (url == mPresenter.urlTopUpAddress()){
                        activity?.finish()
                    }
                }

            }
        }
    }

    private fun initWebView() {
        binding.webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    view.loadUrl(url!!)
                    this@PhoneRechareCardFragment.url = url
                    return true
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressLoadingWebview.visibility = View.GONE
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    showErrorDialog()
                    super.onReceivedError(view, request, error)
                }

                override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
                    resend?.sendToTarget();
                }

            }
            addJavascriptInterface(CaptureClickJavascriptInterface(), "Android")
            loadUrl(mPresenter.urlTopUpAddress())
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
        }
    }


    inner class CaptureClickJavascriptInterface {
        @JavascriptInterface
        fun handleButtonClick() {
            PhoneContactDialog(requireContext()) {
                binding.webview.loadUrl("javascript:passDataToWebPageView('${it.name}','${it.phone}')")
            }.show(childFragmentManager, "PhoneRechareCardFragment")
        }

        @JavascriptInterface
        fun handlerHotlineClick(hotline: String) {
            this@PhoneRechareCardFragment.hotline = hotline
            if (allPermissionsGranted()) {
                onPermissionGranted()
            } else {
                permissionRequest.launch(permissions.toTypedArray())
            }

        }

    }

    // The permissions we need for the app to work properly
    private val permissions = mutableListOf(Manifest.permission.CALL_PHONE)

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                onPermissionGranted()
            } else {
                Toast.showToast(activity,resources.getString(R.string.str_no_call_phone_assess))
            }
        }

    // handler after the permission check
    private fun onPermissionGranted() {
        hotline?.let {
            val intent =
                Intent(Intent.ACTION_CALL, Uri.parse("tel:$it"))
            startActivity(intent)
        }

    }
    private fun showErrorDialog() {
        binding.progressLoadingWebview.visibility = View.GONE
        ErrorDialog("Không tải được dữ liệu!").show(childFragmentManager, "QRFragment")
    }
}