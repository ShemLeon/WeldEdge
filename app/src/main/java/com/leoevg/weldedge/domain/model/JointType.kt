package com.leoevg.weldedge.domain.model

import androidx.annotation.DrawableRes
import com.leoevg.weldedge.R

enum class JointType(
    val id: String,
    val nameRu: String,
    val nameEn: String,
    @DrawableRes val iconRes: Int
) {
    BUTT("butt", "Стыковой", "Butt", R.drawable.joint_butt),
    T_JOINT("t_joint", "Тавровый", "T-joint", R.drawable.joint_t),
    CORNER("corner", "Угловой", "Corner", R.drawable.joint_corner),
    LAP("lap", "Нахлесточный", "Lap", R.drawable.joint_lap);

    fun getLocalizedName(language: String): String {
        return when (language.uppercase()) {
            "EN" -> nameEn
            else -> nameRu
        }
    }

    companion object {
        //  функция для поиска enum по его ID
        fun fromId(id: String): JointType? = entries.find { it.id == id }
    }

    /***
    // Сохранено в WeldingParams: jointType = "butt"
    // Нужно получить полный объект JointType:
    val jointType = JointType.fromId("butt")  // вернёт JointType.BUTT

    // Теперь можем использовать:
    jointType?.nameRu        // "Стыковой"
    jointType?.nameEn        // "Butt"
    jointType?.iconRes       // R.drawable.joint_butt
    jointType?.getLocalizedName("EN")  // "Butt"
    */
}