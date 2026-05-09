package com.leoevg.weldedge.data.repository

import android.R.attr.thickness
import android.content.Context
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.domain.model.AlloyCategory
import com.leoevg.weldedge.domain.model.AlloysDatabase
import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.domain.model.WeldingTypeItem
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import com.leoevg.weldedge.domain.repository.ReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import javax.inject.Inject

class DbManagerRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) : DbManagerRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var _database: AlloysDatabase

    // поход в бд
    override suspend fun onCreateInitialState(): Result<BeAvailableWeldingParams> {
        return Result.success(converter(getDatabase()))
    }

    fun converter(alloysDatabase: AlloysDatabase): BeAvailableWeldingParams {
        // потом перемапить
        val metalType: List<MetalGroup> = alloysDatabase.metals1
        val markMetal: List<String> = alloysDatabase.metals1.flatMap { it.markMetal }
        val thickness: List<String> = alloysDatabase.thickness.map { it.toString() }
        val jointType: List<JointType> = alloysDatabase.jointType
        val typeOfWeld: List<EdgePreparationGroup> =
            alloysDatabase.edgePreparation // responsibility (FW / BW)
        val edgePreparation: List<EdgePreparationItem> =
            alloysDatabase.edgePreparation.flatMap { it.array }
        val weldingType: List<WeldingTypeItem> = alloysDatabase.weldingType
        val engineerName: List<String> = listOf() // пока не актуально
        val standard: List<String> = listOf() // пока не актуально

        return BeAvailableWeldingParams(
            metalType,
            markMetal,
            thickness,
            jointType,
            typeOfWeld,
            edgePreparation,
            weldingType
        )

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