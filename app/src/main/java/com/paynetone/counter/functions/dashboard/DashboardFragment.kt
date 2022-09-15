package com.paynetone.counter.functions.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.core.base.viper.ViewFragment
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.paynetone.counter.R
import com.paynetone.counter.databinding.DashboardFragmentBinding
import com.paynetone.counter.model.EmployeeModel
import com.paynetone.counter.model.PaynetModel
import com.paynetone.counter.model.request.DashboardRequest
import com.paynetone.counter.model.response.DashboardData
import com.paynetone.counter.model.response.Statistical
import com.paynetone.counter.utils.Constants
import com.paynetone.counter.utils.NumberUtils
import com.paynetone.counter.utils.SharedPref
import com.paynetone.counter.utils.setSingleClick
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : ViewFragment<DashboardContract.Presenter>(), DashboardContract.View  {
    private lateinit var binding: DashboardFragmentBinding
    private var paynetModel: PaynetModel? = null
    private var employeeModel: EmployeeModel?=null
    private val sharedPref by lazy { SharedPref(requireActivity()) }
    private val adapter by lazy { DashboardOptionAdapter(requireContext()){
        binding.cardView.visibility = View.GONE
        when(it.id){
            0->{
                binding.tvOption.text = getTimeDate()
                requestDashboard(type = Constants.DASHBOARD_DAY)
            }
            1->{
                binding.tvOption.text = it.name
                requestDashboard(type = Constants.DASHBOARD_WEEK)
            }
            2->{
                binding.tvOption.text = it.name
                requestDashboard(type = Constants.DASHBOARD_MONTH)
            }
            3->{
                binding.tvOption.text = it.name
                requestDashboard(type = Constants.DASHBOARD_QUARTER)
            }
        }
    } }

    private val count = 3

    companion object {
        val instance: DashboardFragment
            get() = DashboardFragment()
    }

    override fun getLayoutId(): Int = R.layout.dashboard_fragment

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
        requestDashboard()
        initAdapter()
        binding.apply {
            tvOption.text = getTimeDate()
            tvOption.setSingleClick {
                if (cardView.visibility == View.GONE) cardView.visibility = View.VISIBLE
                else if (cardView.visibility == View.VISIBLE) cardView.visibility = View.GONE
            }
            initChart(chartPayment)
            initChart(chartService)

        }

    }
    private fun initChart(chart:CombinedChart){

        // draw bars behind lines
        chart.drawOrder = arrayOf(
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.BUBBLE,
            CombinedChart.DrawOrder.CANDLE,
            CombinedChart.DrawOrder.LINE,
            CombinedChart.DrawOrder.SCATTER
        )

        chart.legend.apply {
            isWordWrapEnabled = true
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            isEnabled = false
            setDrawInside(false)
        }
        chart.axisRight.apply {
            setDrawGridLines(false)
            axisMinimum = 0f // this replaces setStartAtZero(true)
        }

        chart.axisLeft.apply {
            setDrawGridLines(false)
            axisMinimum = 0f // this replaces setStartAtZero(true)
        }

        val data = CombinedData()

        data.setData(generateLineData(50f,100f,150f))
        data.setData(generateBarData(1500f,2500f,3500f))
        data.setDrawValues(false)

        chart.axisLeft.apply {
            axisMaximum = 5000f
            axisMinimum = 500f
            setLabelCount(9, false)
        }

        chart.axisRight.apply {
            axisMaximum = 450f
            axisMinimum = 0f
            setLabelCount(9, false)
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = MyXAxisFormatter()
            setLabelCount(3,false)
            setCenterAxisLabels(true)
            setDrawAxisLine(false)
            axisMaximum = data.xMax + 0.25f
        }
        chart.apply {
            description.isEnabled = false
            setBackgroundColor(Color.WHITE)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            isHighlightFullBarEnabled = false

            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            isScaleXEnabled = false
            isScaleYEnabled = false
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            setData(data)
            invalidate()
        }

    }

    private fun generateLineData(data1:Float,data2:Float,data3:Float): LineData {
        val d = LineData()
        val entries = ArrayList<Entry>()
        if (data1>0){
            entries.add(Entry( 0.5f, data1))
        }
        if (data2>0){
            entries.add(Entry( 1.5f, data2))
        }
        if (data3>0){
            entries.add(Entry( 2.5f, data3))
        }

        val set = LineDataSet(entries, "Line DataSet")
        set.color = resources.getColor(R.color.close)
        set.lineWidth = 0.5f
        set.setCircleColor(resources.getColor(R.color.close))
        set.circleRadius = 4f
        set.fillColor = resources.getColor(R.color.white)
        set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set.setDrawValues(false)
        set.axisDependency = YAxis.AxisDependency.RIGHT
        d.addDataSet(set)
        return d
    }

    private fun generateBarData(data1:Float,data2:Float,data3:Float): BarData {
        val entries1 = ArrayList<BarEntry>()
        val entries2 = ArrayList<BarEntry>()
        val entries3 = ArrayList<BarEntry>()

        entries1.add(BarEntry(0f,data1))
        entries2.add(BarEntry(0f,data2))
        entries3.add(BarEntry(0f,data3))

        val set1 = BarDataSet(entries1, "DataSet 1")
        val set2 = BarDataSet(entries2, "DataSet 2")
        val set3 = BarDataSet(entries3, "DataSet 3")
        val dataSets = ArrayList<IBarDataSet>()
        set1.color = resources.getColor(R.color.colorPrimary)
        set2.color = resources.getColor(R.color.colorPrimary)
        set3.color = resources.getColor(R.color.colorPrimary)
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val groupSpace = 0.06f
        val barSpace = 0.12f // x2 dataset
        val barWidth = 0.2f // x2 dataset
        val data = BarData(dataSets)
        data.groupBars(0f, groupSpace, barSpace) // start at x = 0
        data.setValueTextSize(10f)
        data.barWidth = .3f
        return data
    }
    private fun getTimeDate():String{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return "$day"+"/"+"${month.plus(1)}"+"/"+"$year"
    }
    private fun initAdapter(){
        binding.apply {
            recycle.adapter =  this@DashboardFragment.adapter
        }
    }
    private fun requestDashboard(type:String = Constants.DASHBOARD_DAY){
        mPresenter.requestDashboard(DashboardRequest(merchantID = paynetModel?.merchantID ?: 0,
            empID = employeeModel?.id ?: 0,
            dateMode = type))
    }

    override fun requestError(message: String) {

    }

    override fun showDashboardData(response: ArrayList<DashboardData>?) {
        response?.let {
            if (response.size>0){
                binding.apply {
                    tvAmountTransactionPayment.text = response[0].countQROnline.toString()
                    tvSumAmountPayment.text = NumberUtils.formatPriceNumber(response[0].sumQROnline) + " đ"
                    tvAmountTransactionService.text = response[0].countGTGT.toString()
                    tvSumAmountService.text = NumberUtils.formatPriceNumber(response[0].sumGTGT) + " đ"
                }
            }
        }
    }

    override fun showStatistical(response: ArrayList<Statistical>?) {
        response?.let {
            try {

                binding.apply {
                    val dataPayment = CombinedData()
                    dataPayment.setData(generateLineData(response[0].countQROnline.toFloat(),
                        response[1].countQROnline.toFloat(),
                        response[2].countQROnline.toFloat()))
                    dataPayment.setData(generateBarData(response[0].sumQROnline.toFloat().div(1000),
                        response[1].sumQROnline.toFloat().div(1000),
                        response[2].sumQROnline.toFloat().div(1000)))

                    dataPayment.setDrawValues(false)

                    chartPayment.data = dataPayment
                    chartPayment.invalidate()
                    chartPayment.animateY(1500);

                    val dataService = CombinedData()
                    dataService.setData(generateLineData(response[0].countGTGT.toFloat(),
                        response[1].countGTGT.toFloat(),
                        response[2].countGTGT.toFloat()))
                    dataService.setData(generateBarData(response[0].sumGTGT.toFloat().div(1000),
                        response[1].sumGTGT.toFloat().div(1000),
                        response[2].sumGTGT.toFloat().div(1000)))

                    dataService.setDrawValues(false)

                    chartService.data = dataService
                    chartService.invalidate()
                    chartService.animateY(1500);
                }


            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
}