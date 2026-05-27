package com.leoevg.weldedge.di

import android.content.Context
import com.leoevg.weldedge.data.local.PreferencesManager
import com.leoevg.weldedge.data.repository.AlloysDatabaseRepository
import com.leoevg.weldedge.data.repository.DbManagerRepositoryImpl
import com.leoevg.weldedge.data.repository.ReportRepositoryImpl
import com.leoevg.weldedge.domain.repository.DbManagerRepository
import com.leoevg.weldedge.domain.repository.ReportRepository
import com.leoevg.weldedge.domain.usecase.GenerateReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAlloysDatabaseRepository(@ApplicationContext context: Context): AlloysDatabaseRepository {
        return AlloysDatabaseRepository(context)
    }

    @Provides
    @Singleton
    fun provideDbManagerRepository(impl: DbManagerRepositoryImpl): DbManagerRepository = impl

    @Provides
    @Singleton
    fun provideReportRepository(impl: ReportRepositoryImpl): ReportRepository = impl

    @Provides
    @Singleton
    fun provideGenerateReportUseCase(repository: ReportRepository): GenerateReportUseCase {
        return GenerateReportUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}