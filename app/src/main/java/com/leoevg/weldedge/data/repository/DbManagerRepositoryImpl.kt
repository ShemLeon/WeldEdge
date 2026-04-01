package com.leoevg.weldedge.data.repository

import android.content.Context
import com.leoevg.weldedge.domain.model.AlloyCategory
import com.leoevg.weldedge.domain.model.AlloysDatabase
import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import com.leoevg.weldedge.domain.repository.ReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import javax.inject.Inject

class DbManagerRepositoryImpl  @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DbManagerRepository {
    override suspend fun onCreateInitialState(): Result<BeAvailableWeldingParams> {
        // поход в бд
    }

    private val json = Json { ignoreUnknownKeys = true }
    private var _database: AlloysDatabase? = null

    fun converter(alloysDatabase: AlloysDatabase): BeAvailableWeldingParams{
        // потом перемапить
        alloysDatabase
        val metalTypeMapped: List<Pair<Int, List<MetalGroup>>> = alloysDatabase.metals1.map { listOf<it>() }

    }

    fun getDatabase(): AlloysDatabase {
        if (_database == null) {
            context.assets.open(AlloysDatabase.ASSET_PATH).use { input ->
                _database = json.decodeFromString<AlloysDatabase>(
                    InputStreamReader(input).readText()
                )
            }
        }
        return _database!!
    }

    fun mapToAlloyCategory(groupId: String): AlloyCategory = when (groupId) {
        "AL" -> AlloyCategory.ALUMINIUM
        "SS" -> AlloyCategory.STAINLESS_STEEL
        "CS" -> AlloyCategory.STEEL
        else -> AlloyCategory.STEEL
    }

}