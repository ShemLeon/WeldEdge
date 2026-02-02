package com.leoevg.weldedge.domain.model

import androidx.annotation.StringRes
import com.leoevg.weldedge.R

enum class Responsibility(
    val id: String,
    @StringRes val nameRes: Int
) {
    STRESS("stress", R.string.responsibility_stress),
    SIMPLE("simple", R.string.responsibility_simple);

    companion object {
        fun fromId(id: String): Responsibility? = entries.find { it.id == id }
    }
}
