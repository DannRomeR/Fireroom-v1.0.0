package com.example.fireroomv100.model.service.module

import com.example.fireroomv100.model.service.AccountService
import com.example.fireroomv100.model.service.LogService
import com.example.fireroomv100.model.service.impl.AccountServiceImpl
import com.example.fireroomv100.model.service.impl.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun providerAccountService(impl: AccountServiceImpl) : AccountService
    @Binds abstract fun providerLogService(impl: LogServiceImpl): LogService
}