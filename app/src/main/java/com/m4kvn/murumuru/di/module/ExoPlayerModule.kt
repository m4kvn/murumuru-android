package com.m4kvn.murumuru.di.module

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExoPlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayer(context: Context): SimpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(context,
                    DefaultTrackSelector(null),
                    DefaultLoadControl())

    @Provides
    @Singleton
    fun provideDataSourceFactory(context: Context): DataSource.Factory =
            DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "murumuru"), null)
}