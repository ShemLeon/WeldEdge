package com.leoevg.weldedge.domain.repository

import com.leoevg.weldedge.domain.model.WeldingParams

interface ReportRepository {
    suspend fun generateAndOpenReport(params: WeldingParams): Result<Unit>
}
