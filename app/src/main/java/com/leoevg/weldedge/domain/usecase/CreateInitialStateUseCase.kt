package com.leoevg.weldedge.domain.usecase

import com.leoevg.weldedge.domain.model.WeldingParams
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import com.leoevg.weldedge.domain.repository.ReportRepository


class CreateInitialStateUseCase(private val repository: DbManagerRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.onCreateInitialState()
    }
}
