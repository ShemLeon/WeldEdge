package com.leoevg.weldedge.data.repository

import com.leoevg.weldedge.domain.model.BeAvailableWeldingParams
import com.leoevg.weldedge.domain.model.EdgePreparationGroup
import com.leoevg.weldedge.domain.model.EdgePreparationItem
import com.leoevg.weldedge.domain.model.JointType
import com.leoevg.weldedge.domain.model.MetalGroup
import com.leoevg.weldedge.domain.model.WeldingTypeItem
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import javax.inject.Inject

class DbManagerRepositoryImpl @Inject constructor(
    private val alloysDatabaseRepository: AlloysDatabaseRepository
) : DbManagerRepository {

    override suspend fun onCreateInitialState(): Result<BeAvailableWeldingParams> {
        val db = alloysDatabaseRepository.getDatabase()
        val metalType: List<MetalGroup> = db.metals1
        val markMetal: List<String> = db.metals1.flatMap { it.markMetal }
        val thickness: List<String> = db.thickness.map { it.toString() }
        val jointType: List<JointType> = db.jointType
        val typeOfWeld: List<EdgePreparationGroup> = db.edgePreparation
        val edgePreparation: List<EdgePreparationItem> = db.edgePreparation.flatMap { it.array }
        val weldingType: List<WeldingTypeItem> = db.weldingType
        val standard: List<String> = listOf("AWS", "ГОСТ")

        return Result.success(
            BeAvailableWeldingParams(
                metalType = metalType,
                markMetal = markMetal,
                thickness = thickness,
                jointType = jointType,
                typeOfWeld = typeOfWeld,
                edgePreparation = edgePreparation,
                weldingType = weldingType,
                standard = standard
            )
        )
    }
}