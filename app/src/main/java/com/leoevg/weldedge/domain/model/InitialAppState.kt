package com.leoevg.weldedge.domain.model

data class InitialAppState(
    val weldingFormParams: BeAvailableWeldingParams,
    val savedParams: WeldingParams
)
