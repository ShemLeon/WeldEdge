package com.leoevg.weldedge.domain.repository

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams

interface DbManagerRepository {
    suspend fun onCreateInitialState(): Result<BeAvailableWeldingParams>
}