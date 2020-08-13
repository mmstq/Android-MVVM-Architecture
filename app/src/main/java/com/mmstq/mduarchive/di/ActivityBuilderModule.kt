package com.mmstq.mduarchive.di

import com.mmstq.mduarchive.activity.HomeScreen
import com.mmstq.mduarchive.activity.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {


    @ContributesAndroidInjector
    abstract fun contributeHomeActivity():HomeScreen

    @ContributesAndroidInjector(
        modules = [
            NoticeApiModule::class,
            MainFragmentModule::class

        ]
    )
    abstract fun contributeMainActivity():MainActivity

}