package com.leoevg.weldedge.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.leoevg.weldedge.R

enum class JointType(
    val id: String,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) {
    BUTT("butt", R.string.joint_butt, R.drawable.joint_butt),
    T_JOINT("t_joint", R.string.joint_t, R.drawable.joint_t),
    CORNER("corner", R.string.joint_corner, R.drawable.joint_corner),
    LAP("lap", R.string.joint_lap, R.drawable.joint_lap);

    companion object {
        //  функция для поиска enum по его ID
        fun fromId(id: String): JointType? = entries.find { it.id == id }
    }
}