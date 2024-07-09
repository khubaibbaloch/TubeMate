package com.TubeMateApp.tubemate.TubeMateApp.domain

import android.content.Context
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate.DownloadHelper
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate.SharedPreferencesHelper
import com.TubeMateApp.tubemate.TubeMateApp.helperClasses.TubeMate.TubeMateRepository
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
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDownloadHelper(@ApplicationContext context: Context): DownloadHelper {
        return DownloadHelper(context)
    }

    @Provides
    @Singleton
    fun provideTubeMateRepository(
        @ApplicationContext context: Context,
        downloadHelper: DownloadHelper
    ): TubeMateRepository {
        return TubeMateRepository(context, downloadHelper)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(context)
    }
}
