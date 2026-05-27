package com.leoevg.weldedge.domain.usecase

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import javax.inject.Inject

class CreateInitialStateUseCase @Inject constructor(
    private val repository: DbManagerRepository
) {
    suspend operator fun invoke(): Result<BeAvailableWeldingParams> {
        return repository.onCreateInitialState()
    }
}