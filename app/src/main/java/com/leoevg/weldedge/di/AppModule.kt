package com.leoevg.weldedge.di

import com.leoevg.weldedge.data.repository.ReportRepositoryImpl
import com.leoevg.weldedge.domain.repository.ReportRepository
import com.leoevg.weldedge.domain.usecase.GenerateReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideReportRepository(impl: ReportRepositoryImpl): ReportRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideGenerateReportUseCase(repository: ReportRepository): GenerateReportUseCase {
        return GenerateReportUseCase(repository)
    }
}
