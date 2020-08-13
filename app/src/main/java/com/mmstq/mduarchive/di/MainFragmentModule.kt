package com.mmstq.mduarchive.di

import com.mmstq.mduarchive.fragment.MDU
import com.mmstq.mduarchive.fragment.UIET
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun getContributeFragment(): UIET

    @ContributesAndroidInjector
    abstract fun contributeMDUFragment(): MDU
}