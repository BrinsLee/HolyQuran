package com.ihyas.soharamkarubar.ui.zakat

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.FragmentZakatBinding
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.DoubleExtensions.limitTo2Decimal
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import dagger.hilt.android.AndroidEntryPoint
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

@AndroidEntryPoint
class ZakatFragment : BaseFragment<FragmentZakatBinding, ZakatViewModel>() {

    private var dataBaseFile: DataBaseFile? = null

    private var totalAssets: Double = 0.0
    private var totalZakatDue: Double = 0.0
    private var cashInHand: Double = 0.0
    private var cashInBank: Double = 0.0

    private var goldValuePrice: Double = 0.0
    private var goldWeightPrice: Double = 0.0
    private var goldWeight24CaratUnit: Double = 0.0
    private var goldWeight22CaratUnit: Double = 0.0
    private var goldWeight18CaratUnit: Double = 0.0
    private var total24CaratGoldPrice: Double = 0.0
    private var total22CaratGoldPrice: Double = 0.0
    private var total18CaratGoldPrice: Double = 0.0
    private var totalGold: Double = 0.0

    private var silverValuePrice: Double = 0.0
    private var silverWeightPrice: Double = 0.0
    private var silverWeightQuentity: Double = 0.0
    private var silverWeightAndQuentity: Double = 0.0
    private var investmentShares: Double = 0.0
    private var investmentCashInBank: Double = 0.0
    private var investmentShareAndBank: Double = 0.0
    private var propertyRentIncom: Double = 0.0
    private var businessCash: Double = 0.0
    private var businessGoodsStocks: Double = 0.0
    private var businessCashGoods: Double = 0.0
    private var agriRainWater: Double = 0.0

    private var agriIrrigationWater: Double = 0.0
    private var agriBothWater: Double = 0.0
    private var agriRainWaterPercentage: Double = 0.0
    private var agriIrrigationWaterPercentage: Double = 0.0
    private var agriBothWaterPercentage: Double = 0.0
    private var totalAgri: Double = 0.0

    private var cattleCamel: Double = 0.0
    private var cattleCow: Double = 0.0
    private var cattleSheep: Double = 0.0
    private var totalCattle: Double = 0.0
    private var preciousStones: Double = 0.0
    private var totalPreciousStones: Double = 0.0
    private var otherPension: Double = 0.0
    private var otherLoan: Double = 0.0
    private var otherAssets: Double = 0.0
    private var totalOther: Double = 0.0

    // payable
    private var payableCreditCard: Double = 0.0
    private var payableHome: Double = 0.0
    private var payableCar: Double = 0.0
    private var payableBusiness: Double = 0.0
    private var payableFamilyDebt: Double = 0.0
    private var payableOtherDebt: Double = 0.0
    private var totalPayable: Double = 0.0
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    override val viewModel: ZakatViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.fragment_zakat

    override fun onReady(savedInstanceState: Bundle?) {

        dataBaseFile = DataBaseFile(context)

        setListener()
        setSpinner()
        setInfoListner()
        setZakatCalculationListener()
        loadAds()
        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/
       /*
        */
    }

    private fun setListener() {
        binding.moneyTitleCL.setSafeOnClickListener {
            if (binding.moneyChildCL.isVisible) {
                binding.moneyChildCL.visibility = View.GONE
            } else {
                binding.moneyChildCL.visibility = View.VISIBLE
            }
        }

        binding.goldTitleCL.setSafeOnClickListener {
            if (binding.goldChildHeadingCL.isVisible) {
                binding.goldChildHeadingCL.visibility = View.GONE

            } else {
                binding.goldChildHeadingCL.visibility = View.VISIBLE
            }

        }

        binding.silverTitleCL.setSafeOnClickListener {
            if (binding.silverChildHeadingCL.isVisible) {
                binding.silverChildHeadingCL.visibility = View.GONE
            } else {
                binding.silverChildHeadingCL.visibility = View.VISIBLE
            }
        }

        binding.investmentTitleCL.setSafeOnClickListener {
            if (binding.investmentChildHeadingCL.isVisible) {
                binding.investmentChildHeadingCL.visibility = View.GONE
            } else {
                binding.investmentChildHeadingCL.visibility = View.VISIBLE
            }
        }

        binding.propertiesHeadingCL.setSafeOnClickListener {
            if (binding.propertiesChildHeadingCL.isVisible) {
                binding.propertiesChildHeadingCL.visibility = View.GONE
            } else {
                binding.propertiesChildHeadingCL.visibility = View.VISIBLE
            }
        }

        binding.businessTitleCL.setSafeOnClickListener {
            if (binding.businessChildHeadingCL.isVisible) {
                binding.businessChildHeadingCL.visibility = View.GONE
            } else {
                binding.businessChildHeadingCL.visibility = View.VISIBLE
            }
        }

        binding.agricultureTitleCL.setSafeOnClickListener {
            if (binding.agricultureChildCL.isVisible) {
                binding.agricultureChildCL.visibility = View.GONE
            } else {
                binding.agricultureChildCL.visibility = View.VISIBLE
            }
        }
        binding.cattleTitleCL.setSafeOnClickListener {
            if (binding.cattleTypeCL.isVisible) {
                binding.cattleTypeCL.visibility = View.GONE
            } else {
                binding.cattleTypeCL.visibility = View.VISIBLE
            }

        }

        binding.preciousStonesTitleCL.setSafeOnClickListener {
            if (binding.preciousStonesChildCL.isVisible) {
                binding.preciousStonesChildCL.visibility = View.GONE
            } else {
                binding.preciousStonesChildCL.visibility = View.VISIBLE
            }
        }

        binding.otherTitleCL.setSafeOnClickListener {
            if (binding.otherTypeCL.isVisible) {
                binding.otherTypeCL.visibility = View.GONE
            } else {
                binding.otherTypeCL.visibility = View.VISIBLE
            }
        }

        binding.payableTitleCL.setSafeOnClickListener {
            if (binding.payableTypeCL.isVisible) {
                binding.payableTypeCL.visibility = View.GONE
            } else {
                binding.payableTypeCL.visibility = View.VISIBLE
            }
        }

        binding.goldRadioBtn.setOnCheckedChangeListener { group, goldCheckedId ->
            if (goldCheckedId == binding.radioValue.id) {

                binding.goldValueCL.visibility = View.VISIBLE
                binding.goldWeightCL.visibility = View.GONE
            } else if (goldCheckedId == binding.radioWeight.id) {
                binding.goldValueCL.visibility = View.GONE
                binding.goldWeightCL.visibility = View.VISIBLE
            }
        }
        binding.silverRadioBtn.setOnCheckedChangeListener { group, silverCheckedId ->
            if (silverCheckedId == binding.silverValueRB.id) {

                binding.silverValueCL.visibility = View.VISIBLE
                binding.silverWeightCL.visibility = View.GONE
            } else if (silverCheckedId == binding.silverWeightRB.id) {
                binding.silverWeightCL.visibility = View.VISIBLE
                binding.silverValueCL.visibility = View.GONE
            }
        }

        binding.backBtn.setSafeOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setSpinner() {
        val zakatWeightUnit = resources.getStringArray(R.array.zakat_weight_unit_array)

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.zakat_weight_unit_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


                binding.goldweightUnitSpinner.adapter = adapter
                binding.silverWeightUnitSpinner.adapter = adapter
            }
        }



        binding.goldweightUnitSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    context?.let { (parent?.getChildAt(0) as? TextView)?.setTextColor(it.getColor(R.color.onSurface2)) }
                    binding.gold24CaratWeightUnitTxt.text = zakatWeightUnit[position].toString()
                    binding.gold22CaratWeightUnitTxt.text = zakatWeightUnit[position].toString()
                    binding.gold18CaratWeightUnitTxt.text = zakatWeightUnit[position].toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        binding.silverWeightUnitSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    context?.let { (parent?.getChildAt(0) as? TextView)?.setTextColor(it.getColor(R.color.onSurface2)) }
                    binding.weightUnitTxt.text = zakatWeightUnit[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }


    }

    private fun setInfoListner() {
        val zakatInfoDetail = resources.getStringArray(R.array.zakat_info_detail_array)

        binding.moneyInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.moneyInfoIV)
                    .text(zakatInfoDetail[0])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }

        binding.goldInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.goldInfoIV)
                    .text(zakatInfoDetail[1])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.silverInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.silverInfoIV)
                    .text(zakatInfoDetail[2])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.investmentInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.investmentInfoIV)
                    .text(zakatInfoDetail[3])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.propertyInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.propertyInfoIV)
                    .text(zakatInfoDetail[4])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.businessInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.businessInfoIV)
                    .text(zakatInfoDetail[5])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.agricultureInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.agricultureInfoIV)
                    .text(zakatInfoDetail[6])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.cattleInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.cattleInfoIV)
                    .text(zakatInfoDetail[7])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.preciousStonesInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.preciousStonesInfoIV)
                    .text(zakatInfoDetail[8])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.otherInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.otherInfoIV)
                    .text(zakatInfoDetail[9])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }
        binding.payableInfoIV.setSafeOnClickListener {
            context?.let { it1 ->
                SimpleTooltip.Builder(context)
                    .anchorView(binding.payableInfoIV)
                    .text(zakatInfoDetail[10])
                    .gravity(Gravity.END)
                    .arrowColor(it1.getColor(R.color.onSurface2))
                    .backgroundColor(it1.getColor(R.color.onSurface2))
                    .textColor(it1.getColor(R.color.white))
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
        }

    }

    private fun setZakatCalculationListener() {
        binding.cashInHandET.doAfterTextChanged {
            cashInHand = if (binding.cashInHandET.text.toString().isNotEmpty()) {
                binding.cashInHandET.text.toString().toDouble().limitTo2Decimal()

            } else {
                0.0
            }

            val totalMoney = (cashInBank+cashInHand).limitTo2Decimal().toLong()
            binding.totalMoneyTV.text = totalMoney.toString()
            zakatCalculation()
        }
        binding.cashInBankET.doAfterTextChanged {
            cashInBank = if (binding.cashInBankET.text.toString().isNotEmpty()) {
                binding.cashInBankET.text.toString().toDouble()

            } else {
                0.0
            }

            binding.totalMoneyTV.text = cashInHand.let { cashInBank.plus(it).toLong().toString() }
            zakatCalculation()
        }
        binding.goldValuePriceET.doAfterTextChanged {
            goldValuePrice = if (binding.goldValuePriceET.text.toString().isNotEmpty()) {
                binding.goldValuePriceET.text.toString().toDouble()
            } else {
                0.0
            }

            totalGold = goldValuePrice
            binding.totalGoldTV.text = totalGold.toLong().toString()
            zakatCalculation()
        }
        binding.goldweightUnitPriceET.doAfterTextChanged {
            goldWeightPrice = if (binding.goldweightUnitPriceET.text.toString().isNotEmpty()) {
                binding.goldweightUnitPriceET.text.toString().toDouble()
            } else {
                0.0
            }
            total24CaratGoldPrice = goldWeightPrice.let { goldWeight24CaratUnit.times(it) }
            totalGold = if (goldWeightPrice == 0.0) {
                goldWeightPrice
            } else {
                total22CaratGoldPrice.let { carat24 ->
                    total18CaratGoldPrice.let { carat22 ->
                        total24CaratGoldPrice.plus(
                            carat24
                        ).plus(carat22)
                    }
                }
            }
            binding.goldWeight24CaratPriceTV.text = total24CaratGoldPrice.toLong().toString()
            binding.totalGoldTV.text = totalGold.toLong().toString()
            zakatCalculation()
        }
        binding.gold24CaratweightPerUnitPriceET.doAfterTextChanged {
            goldWeight24CaratUnit =
                if (binding.gold24CaratweightPerUnitPriceET.text.toString().isNotEmpty()) {
                    binding.gold24CaratweightPerUnitPriceET.text.toString().toDouble()
                } else {
                    0.0
                }
            total24CaratGoldPrice = goldWeightPrice.let { carat24 ->
                goldWeight24CaratUnit.times(carat24)
            }
            totalGold = total22CaratGoldPrice.let { carat24 ->
                total18CaratGoldPrice.let { carat22 ->
                    total24CaratGoldPrice.plus(carat24).plus(carat22)
                }
            }
            binding.goldWeight24CaratPriceTV.text = total24CaratGoldPrice.toLong().toString()
            binding.totalGoldTV.text = totalGold.toLong().toString()
            zakatCalculation()
        }
        binding.gold22CaratweightPerUnitPriceET.doAfterTextChanged {
            goldWeight22CaratUnit =
                if (binding.gold22CaratweightPerUnitPriceET.text.toString().isNotEmpty()) {
                    binding.gold22CaratweightPerUnitPriceET.text.toString().toDouble()
                } else {
                    0.0
                }

            total22CaratGoldPrice = goldWeightPrice.let { carat22 ->
                goldWeight22CaratUnit.times(carat22)
            }
            total22CaratGoldPrice = total22CaratGoldPrice.times(8.4).div(100).let {
                total22CaratGoldPrice.minus(it)
            }

            totalGold = total22CaratGoldPrice.let { carat24 ->
                total18CaratGoldPrice.let { carat18 ->
                    total24CaratGoldPrice.plus(carat24).plus(carat18)
                }
            }
            binding.goldWeight22CaratPriceTV.text = total22CaratGoldPrice.toLong().toString()
            binding.totalGoldTV.text = totalGold.toLong().toString()
            zakatCalculation()
        }
        binding.gold18CaratweightPerUnitPriceET.doAfterTextChanged {
            goldWeight18CaratUnit =
                if (binding.gold18CaratweightPerUnitPriceET.text.toString().isNotEmpty()) {
                    binding.gold18CaratweightPerUnitPriceET.text.toString().toDouble()
                } else {
                    0.0
                }
            total18CaratGoldPrice = goldWeightPrice.let { carat18 ->
                goldWeight18CaratUnit.times(carat18)
            }
            total18CaratGoldPrice =
                total18CaratGoldPrice.times(25).div(100).let {
                    total18CaratGoldPrice.minus(
                        it
                    )
                }
            totalGold = total24CaratGoldPrice.let { carat22 ->
                total22CaratGoldPrice.let { carat18 ->
                    total18CaratGoldPrice.plus(carat22).plus(carat18)
                }
            }
            binding.goldWeight18CaratPriceTV.text = total18CaratGoldPrice.toLong().toString()
            binding.totalGoldTV.text = totalGold.toLong().toString()
            zakatCalculation()
        }
        binding.silverValuePriceET.doAfterTextChanged {
            silverValuePrice =
                if (binding.silverValuePriceET.text.toString().isNotEmpty()) {
                    binding.silverValuePriceET.text.toString().toDouble()

                } else {
                    0.0
                }
            silverWeightAndQuentity = silverValuePrice
            binding.totalSilverTV.text = silverValuePrice.toLong().toString()
            zakatCalculation()
        }
        binding.silverWeightUnitPriceET.doAfterTextChanged {
            silverWeightPrice =
                if (binding.silverWeightUnitPriceET.text.toString().isNotEmpty()) {
                    binding.silverWeightUnitPriceET.text.toString().toDouble()

                } else {
                    0.0
                }
            silverWeightAndQuentity = silverWeightPrice.let { silverWeight ->
                silverWeightQuentity.times(silverWeight)
            }
            binding.totalSilverTV.text = silverWeightAndQuentity.toLong().toString()
            zakatCalculation()
        }
        binding.silverWeightQuentityET.doAfterTextChanged {
            silverWeightQuentity =
                if (binding.silverWeightQuentityET.text.toString().isNotEmpty()) {
                    binding.silverWeightQuentityET.text.toString().toDouble()

                } else {
                    0.0
                }
            silverWeightAndQuentity = silverWeightPrice.let { silverWeightQ ->
                silverWeightQuentity.times(silverWeightQ)
            }
            binding.totalSilverTV.text = silverWeightAndQuentity.toLong().toString()
            zakatCalculation()
        }
        binding.sharesET.doAfterTextChanged {
            investmentShares = if (binding.sharesET.text.toString().isNotEmpty()) {
                binding.sharesET.text.toString().toDouble()

            } else {
                0.0
            }
            investmentShareAndBank = investmentShares.let {
                investmentCashInBank.plus(it)
            }
            binding.totalInvestmentTV.text = investmentShareAndBank.toLong().toString()
            zakatCalculation()
        }
        binding.otherInvestmentET.doAfterTextChanged {
            investmentCashInBank = if (binding.otherInvestmentET.text.toString().isNotEmpty()) {
                binding.otherInvestmentET.text.toString().toDouble()

            } else {
                0.0
            }
            investmentShareAndBank = investmentShares.let {
                investmentCashInBank.plus(it)
            }
            binding.totalInvestmentTV.text = investmentShareAndBank.toLong().toString()
            zakatCalculation()
        }
        binding.propertyRentET.doAfterTextChanged {
            propertyRentIncom = if (binding.propertyRentET.text.toString().isNotEmpty()) {
                binding.propertyRentET.text.toString().toDouble()

            } else {
                0.0
            }
            binding.totalPropertyTV.text = propertyRentIncom.toLong().toString()
            zakatCalculation()
        }
        binding.businessCashET.doAfterTextChanged {
            businessCash = if (binding.businessCashET.text.toString().isNotEmpty()) {
                binding.businessCashET.text.toString().toDouble()

            } else {
                0.0
            }
            businessCashGoods = businessCash.let { businessGoodsStocks.plus(it) }
            binding.totalBusinessTV.text = businessCashGoods.toLong().toString()
            zakatCalculation()
        }
        binding.businessGoodsET.doAfterTextChanged {
            businessGoodsStocks = if (binding.businessGoodsET.text.toString().isNotEmpty()) {
                binding.businessGoodsET.text.toString().toDouble()

            } else {
                0.0
            }
            businessCashGoods = businessCash.let {
                businessGoodsStocks.plus(it)
            }
            binding.totalBusinessTV.text = businessCashGoods.toLong().toString()
            zakatCalculation()
        }
        binding.agriRainET.doAfterTextChanged {
            agriRainWater = if (binding.agriRainET.text.toString().isNotEmpty()) {
                binding.agriRainET.text.toString().toDouble()
            } else {
                0.0
            }

            agriRainWaterPercentage = agriRainWater.times(10).div(100)
            totalAgri = agriRainWater.let { agriIrrigation ->
                agriIrrigationWater.let { agriBoth ->
                    agriBothWater.plus(agriIrrigation).plus(agriBoth)

                }
            }
            binding.totalAgriTV.text = totalAgri.toLong().toString()
            zakatCalculation()
        }
        binding.agriIrrigationET.doAfterTextChanged {
            agriIrrigationWater = if (binding.agriIrrigationET.text.toString().isNotEmpty()) {
                binding.agriIrrigationET.text.toString().toDouble()
            } else {
                0.0
            }
            agriIrrigationWaterPercentage = agriIrrigationWater.times(5).div(100)

            totalAgri = agriRainWater.let { agriIrrigation ->
                agriIrrigationWater.let { agriBoth ->
                    agriBothWater.plus(agriIrrigation).plus(agriBoth)
                }
            }
            binding.totalAgriTV.text = totalAgri.toLong().toString()
            zakatCalculation()
        }
        binding.agriBothET.doAfterTextChanged {
            agriBothWater = if (binding.agriBothET.text.toString().isNotEmpty()) {
                binding.agriBothET.text.toString().toDouble()
            } else {
                0.0
            }
            agriBothWaterPercentage = agriBothWater.times(7.5).div(100)

            totalAgri = agriRainWater.let { agriIrrigation ->
                agriIrrigationWater.let { agriBoth ->
                    agriBothWater.plus(agriIrrigation).plus(agriBoth)
                }
            }
            binding.totalAgriTV.text = totalAgri.toLong().toString()
            zakatCalculation()
        }
        binding.cattleCamelET.doAfterTextChanged {
            cattleCamel = if (binding.cattleCamelET.text.toString().isNotEmpty()) {
                binding.cattleCamelET.text.toString().toDouble()
            } else {
                0.0
            }
            totalCattle = cattleCamel.let { cow ->

                cattleCow.let { sheep ->
                    cattleSheep.plus(cow).plus(sheep)
                }
            }
            binding.totalCattleTV.text = totalCattle.toLong().toString()
            zakatCalculation()
        }
        binding.cattleCowET.doAfterTextChanged {
            cattleCow = if (binding.cattleCowET.text.toString().isNotEmpty()) {
                binding.cattleCowET.text.toString().toDouble()
            } else {
                0.0
            }
            totalCattle = cattleCamel.let { cow ->

                cattleCow.let { sheep ->
                    cattleSheep.plus(cow).plus(sheep)
                }
            }
            binding.totalCattleTV.text = totalCattle.toLong().toString()
            zakatCalculation()
        }
        binding.cattleSheepET.doAfterTextChanged {
            cattleSheep = if (binding.cattleSheepET.text.toString().isNotEmpty()) {
                binding.cattleSheepET.text.toString().toDouble()
            } else {
                0.0
            }
            totalCattle = cattleCamel.let { cow ->

                cattleCow.let { sheep ->
                    cattleSheep.plus(cow).plus(sheep)
                }
            }
            binding.totalCattleTV.text = totalCattle.toLong().toString()
            zakatCalculation()
        }
        binding.preciousStonesET.doAfterTextChanged {
            preciousStones = if (binding.preciousStonesET.text.toString().isNotEmpty()) {
                binding.preciousStonesET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPreciousStones = preciousStones
            binding.totalPreciousStonesTV.text = totalPreciousStones.toLong().toString()
            zakatCalculation()
        }
        binding.otherPensionET.doAfterTextChanged {
            otherPension = if (binding.otherPensionET.text.toString().isNotEmpty()) {
                binding.otherPensionET.text.toString().toDouble()
            } else {
                0.0
            }
            totalOther = otherPension.let { loan ->

                otherLoan.let { assets ->
                    otherAssets.plus(loan).plus(assets)
                }
            }
            binding.totalOtherTV.text = totalOther.toLong().toString()
            zakatCalculation()
        }
        binding.otherLoanET.doAfterTextChanged {
            otherLoan = if (binding.otherLoanET.text.toString().isNotEmpty()) {
                binding.otherLoanET.text.toString().toDouble()
            } else {
                0.0
            }

            totalOther = otherPension.let { loan ->
                otherLoan.let { assets ->
                    otherAssets.plus(loan).plus(assets)
                }

            }
            binding.totalOtherTV.text = totalOther.toLong().toString()
            zakatCalculation()
        }
        binding.otherAssetsET.doAfterTextChanged {
            otherAssets = if (binding.otherAssetsET.text.toString().isNotEmpty()) {
                binding.otherAssetsET.text.toString().toDouble()
            } else {
                0.0
            }
            totalOther = otherPension.let { loan ->
                otherLoan.let { assets ->
                    otherAssets.plus(loan).plus(assets)
                }
            }
            binding.totalOtherTV.text = totalOther.toLong().toString()
            zakatCalculation()
        }


        // payable
        binding.creditCardPaymentET.doAfterTextChanged {
            payableCreditCard = if (binding.creditCardPaymentET.text.toString().isNotEmpty()) {
                binding.creditCardPaymentET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }
            binding.totalPayableTV.text = totalPayable.toLong().toString()
            zakatCalculation()
        }
        binding.homePaymentET.doAfterTextChanged {
            payableHome = if (binding.homePaymentET.text.toString().isNotEmpty()) {
                binding.homePaymentET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }

            binding.totalPayableTV.text = totalPayable.toLong().toString()
            zakatCalculation()
        }
        binding.carPaymentET.doAfterTextChanged {
            payableCar = if (binding.carPaymentET.text.toString().isNotEmpty()) {
                binding.carPaymentET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }

            binding.totalPayableTV.text = totalPayable.toLong().toString()
            zakatCalculation()
        }
        binding.businessPaymentET.doAfterTextChanged {

            payableBusiness = if (binding.businessPaymentET.text.toString().isNotEmpty()) {
                binding.businessPaymentET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }

            binding.totalPayableTV.text = totalPayable.toLong().toString()
            zakatCalculation()
        }
        binding.familyDebtET.doAfterTextChanged {
            payableFamilyDebt = if (binding.familyDebtET.text.toString().isNotEmpty()) {
                binding.familyDebtET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }
            binding.totalPayableTV.text = totalPayable.toLong().toString()

            zakatCalculation()
        }
        binding.othersDebtET.doAfterTextChanged {

            payableOtherDebt = if (binding.othersDebtET.text.toString().isNotEmpty()) {
                binding.othersDebtET.text.toString().toDouble()
            } else {
                0.0
            }
            totalPayable = payableCreditCard.let { home ->
                payableHome.let { car ->
                    payableCar.let { business ->
                        payableBusiness.let { family ->
                            payableFamilyDebt.let { other ->
                                payableOtherDebt.plus(home).plus(car).plus(business)
                                    .plus(family).plus(other)
                            }
                        }
                    }

                }
            }

            binding.totalPayableTV.text = totalPayable.toLong().toString()
            zakatCalculation()
        }

    }

    private fun zakatCalculation() {
        totalAssets =
            cashInHand.let { cashBank ->
                cashInBank.let { gold ->
                    totalGold.let { silver ->
                        silverWeightAndQuentity.let { investment ->
                            investmentShareAndBank.let { property ->
                                propertyRentIncom.let { business ->
                                    businessCashGoods.let { cattle ->
                                        totalCattle.let { stone ->
                                            totalPreciousStones.let { other ->
                                                totalOther.plus(cashBank).plus(gold)
                                                    .plus(silver)
                                                    .plus(investment).plus(property)
                                                    .plus(business).plus(cattle).plus(stone)
                                                    .plus(other)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        totalAssets = totalPayable.let { totalAssets.minus(it) }
        totalZakatDue = totalAssets.times(2.5).div(100)
        totalZakatDue = totalZakatDue.let { rainPercentage ->
            agriRainWaterPercentage.let { irrigationPercentage ->
                agriIrrigationWaterPercentage.let { bothPercentage ->
                    agriBothWaterPercentage.plus(rainPercentage).plus(irrigationPercentage)
                        .plus(bothPercentage)
                }
            }
        }
        totalAssets = totalAssets.let { agriTotal ->
            totalAgri.plus(agriTotal)
        }
        binding.totalAssetsTV.text = totalAssets.toLong().toString()
        binding.totalZakatDueTV.text = totalZakatDue.toLong().toString()
    }
    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_CALCULATE)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }

}