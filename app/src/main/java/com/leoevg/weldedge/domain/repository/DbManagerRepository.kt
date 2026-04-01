package com.leoevg.weldedge.domain.repository

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams


// интерфейс для будущего создания стейта (менеджер для управления бд)
interface DbManagerRepository {
    suspend fun onCreateInitialState(): Result<BeAvailableWeldingParams>
}