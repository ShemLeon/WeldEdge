package com.leoevg.weldedge.domain.model

import androidx.annotation.StringRes
import com.leoevg.weldedge.R

enum class TypeOfWelds(
    val id: String,
    @StringRes val nameRes: Int
) {
    BW("BW", R.string.type_of_welds_bw),
    FW("FW", R.string.type_of_welds_fw);

    companion object {
        fun fromId(id: String): TypeOfWelds? = entries.find { it.id == id }
    }
}
