package com.reem.currencyconverter.domain.entity.apiError

data class FixerApiError(
    val code: Int?,
    val type: String?,
    val info: String?
)
