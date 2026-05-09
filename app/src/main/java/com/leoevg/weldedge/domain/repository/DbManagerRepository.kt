package com.leoevg.weldedge.domain.repository

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.InitialAppState


// интерфейс для будущего создания стейта (менеджер для управления бд)
interface DbManagerRepository {
    suspend fun xtonCreateInitialState(): Result<InitialAppState>
}
