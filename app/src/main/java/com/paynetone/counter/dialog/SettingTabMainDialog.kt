package com.paynetone.counter.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.google.gson.Gson
import com.paynetone.counter.R
import com.paynetone.counter.adapter.SettingTabMainAdapter
import com.paynetone.counter.databinding.SettingTabMainDialogBinding
import com.paynetone.counter.main.MainActivity
import com.paynetone.counter.main.SplashScreenActivity
import com.paynetone.counter.model.TabMainModel
import com.paynetone.counter.utils.*
import kotlin.math.log

class SettingTabMainDialog(val mContext: Context) : BaseDialogFragment<SettingTabMainDialogBinding>() {

    private lateinit var settingAdapter : SettingTabMainAdapter
    private val defaultTabMain by lazy { arrayListOf(TabMainModel(0,resources.getString(R.string.str_title_tab_qr),Constants.TYPE_TAB_MAIN_QR),
        TabMainModel(1,resources.getString(R.string.str_title_tab_service),Constants.TYPE_TAB_MAIN_SERVICE)) }

    override fun initView(){
        binding.apply {
            var tabMainModels = SharedPref.getInstance(requireContext()).tabMain
            if (tabMainModels==null){
                tabMainModels =  defaultTabMain
            }
            settingAdapter = SettingTabMainAdapter(requireContext(),tabMainModels)
            recycleView.apply {
                adapter = settingAdapter
                orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
                itemLayoutId = R.layout.item_setting_tab_main
                dragListener = object : OnItemDragListener<TabMainModel> {
                    override fun onItemDragged(previousPosition: Int, newPosition: Int, item: TabMainModel) {

                    }

                    override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: TabMainModel) {
                        if (initialPosition != finalPosition){
                            NoticeDialog(requireContext()).show("Bạn có muốn thay đổi không?",false,object :NoticeDialog.OnListenerDialog{
                                override fun onClickYes() {
                                    tabMainModels[initialPosition].position = finalPosition
                                    tabMainModels[finalPosition].position = initialPosition
                                    tabMainModels.sortBy { selector(it) }
                                    SharedPref.getInstance(requireContext()).putString(PrefConst.PREF_IS_TAB_MAIN,Gson().toJson(tabMainModels))
                                    val intent = Intent(activity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)

                                }

                                override fun onClickNo() {
                                    tabMainModels.sortBy {selector(it)}
                                    settingAdapter = SettingTabMainAdapter(requireContext(),tabMainModels)
                                    adapter = settingAdapter

                                }

                            })
                        }

                    }

                }
            }
            ivBack.setSingleClick {
                dismiss()
            }

        }
    }

    fun selector(p: TabMainModel): Int = p.position
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = SettingTabMainDialogBinding.inflate(inflater,container,false)
}