package com.leoevg.weldedge.domain.usecase

import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.repository.ReportRepository


class CreateInitialStateUseCase(private val repository: ReportRepository) {
    suspend operator fun invoke(params: WeldingParams): Result<Unit> {
        return repository.generateAndOpenReport(params)
    }
}
