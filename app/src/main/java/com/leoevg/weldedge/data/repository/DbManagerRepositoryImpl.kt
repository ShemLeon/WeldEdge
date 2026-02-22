package com.leoevg.weldedge.data.repository

import android.content.Context
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import com.leoevg.weldedge.domain.repository.ReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DbManagerRepositoryImpl  @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DbManagerRepository {
    override suspend fun onCreateInitialState() {
        TODO("Not yet implemented")
    }

}