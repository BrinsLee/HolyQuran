package com.ihyas.soharamkarubar.models

import com.google.gson.annotations.SerializedName

data class FastingRuleModel(

	@field:SerializedName("data")
	val sunni: List<SunniItem?>? = null
)

data class SunniItem(

	@field:SerializedName("Ans")
	val ans: String? = null,

	@field:SerializedName("Question")
	val question: String? = null
)
