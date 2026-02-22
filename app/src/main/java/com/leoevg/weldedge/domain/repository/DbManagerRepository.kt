package com.leoevg.weldedge.domain.repository


// интерфейс для будущего создания стейта (менеджер для управления бд)
interface DbManagerRepository {
    suspend fun onCreateInitialState()
}