package com.reem.currencyconverter.domain.models.apiError

data class FixerApiError(
    val code: Int?,
    val type: String?,
    val info: String?
)
