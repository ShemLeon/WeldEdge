package com.leoevg.weldedge.domain.model

/**
 * Класс для сварочной проволоки или электрода (Filler Metal)
 */
data class Wire(
    val name: String,
    val manufacturer: String? = null,
    val chemicalComposition: Map<String, Double>? = null,
    val description: String? = null
)

object Wires {
    // Алюминий
    val ER5356 = Wire("ER 5356")
    val ER4043 = Wire("ER 4043")
    val ER4145 = Wire("ER 4145")
    val ER1100 = Wire("ER 1100")

    // Нержавейка
    val ER308 = Wire("ER 308")
    val ER308L = Wire("ER 308L")
    val ER316L = Wire("ER 316L")
    val ER630 = Wire("ER 630")

    // Сталь
    val AMS6457 = Wire("AMS-6457")
    val ER80S_B2 = Wire("ER80S-B2")
    val ER90S_B3 = Wire("ER90S-B3")
    val ER70S_6 = Wire("ER70S-6")

    /**
     * Подбор проволоки на основе сплава
     */
    fun getRecommendedWire(alloyName: String): Wire? {
        return when (alloyName.uppercase()) {
            "5052", "5086" -> ER5356
            "6061" -> ER4043
            "2014" -> ER4145
            "1100" -> ER1100
            
            "304" -> ER308
            "304L", "308L" -> ER308L
            "316", "316L" -> ER316L
            "15-5 PH", "17-4 PH" -> ER630
            
            "4130" -> AMS6457 // Можно также добавить ER80S-B2 как альтернативу
            "4340" -> ER90S_B3
            "S235JR", "S235J0", "S355JR", "S355J0" -> ER70S_6
            
            else -> null
        }
    }
}
