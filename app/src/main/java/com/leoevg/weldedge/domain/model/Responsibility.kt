package com.leoevg.weldedge.domain.model

enum class Responsibility(
    val id: String,
    val nameRu: String,
    val nameEn: String
) {
    STRESS("stress", "С разделкой", "With groove"),
    SIMPLE("simple", "Без разделки", "Without groove");

    fun getLocalizedName(language: String): String {
        return when (language.uppercase()) {
            "EN" -> nameEn
            else -> nameRu
        }
    }

    companion object {
        fun fromId(id: String): Responsibility? = entries.find { it.id == id }
    }
}
