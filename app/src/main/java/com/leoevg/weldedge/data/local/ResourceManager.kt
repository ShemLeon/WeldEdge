package com.leoevg.weldedge.data.local

import android.content.Context
import androidx.annotation.ArrayRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManager @Inject constructor(val context: Context) {
    fun getList(@ArrayRes id: Int) = context.resources.getStringArray(id).toList()
    fun getString(id: Int) = context.getString(id)
}

// возвращает ресурсы (строки, числа, названия металлов, весь контент)