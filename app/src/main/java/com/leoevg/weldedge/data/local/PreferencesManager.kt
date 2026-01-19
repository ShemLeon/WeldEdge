package com.leoevg.weldedge.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("weld_edge_prefs", Context.MODE_PRIVATE)

    fun saveMetalType(type: String) {
        prefs.edit().putString(KEY_METAL_TYPE, type).apply()
    }

    fun getMetalType(): String? {
        return prefs.getString(KEY_METAL_TYPE, null)
    }

    fun saveLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "RU") ?: "RU"
    }

    fun saveJointType(type: String) {
        prefs.edit().putString(KEY_JOINT_TYPE, type).apply()
    }

    fun getJointType(): String? {
        return prefs.getString(KEY_JOINT_TYPE, null)
    }

    fun saveResponsibility(responsibility: String) {
        prefs.edit().putString(KEY_RESPONSIBILITY, responsibility).apply()
    }

    fun getResponsibility(): String? {
        return prefs.getString(KEY_RESPONSIBILITY, null)
    }

    fun saveStandard(standard: String) {
        prefs.edit().putString(KEY_STANDARD, standard).apply()
    }

    fun getStandard(): String? {
        return prefs.getString(KEY_STANDARD, null)
    }

    fun saveEngineerName(name: String) {
        prefs.edit().putString(KEY_ENGINEER_NAME, name).apply()
    }

    fun getEngineerName(): String {
        return prefs.getString(KEY_ENGINEER_NAME, "") ?: ""
    }

    companion object {
        private const val KEY_METAL_TYPE = "metal_type"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_JOINT_TYPE = "joint_type"
        private const val KEY_RESPONSIBILITY = "responsibility"
        private const val KEY_STANDARD = "standard"
        private const val KEY_ENGINEER_NAME = "engineer_name"
    }
}
