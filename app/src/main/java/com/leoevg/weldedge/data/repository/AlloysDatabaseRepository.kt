package com.leoevg.weldedge.data.repository

import android.content.Context
import com.leoevg.weldedge.domain.model.AlloyCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import com.leoevg.weldedge.domain.model.AlloysDatabase
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlloysDatabaseRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    private var _database: AlloysDatabase? = null

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
