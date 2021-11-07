package com.dx.star

import com.dx.star.model.Repository
import com.dx.star.model.local.AppDatabase
import com.dx.star.model.remote.Api
import com.dx.star.model.remote.createWebService
import com.dx.star.model.remote.getOkHttpClient
import com.dx.star.util.Constant
import com.dx.star.ui.vm.CartViewModel
import com.dx.star.ui.vm.HomeViewModel
import com.dx.star.ui.vm.MyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *
 * java类作用描述
 * zrj 2021/6/26 10:44
 * 更新者 2021/6/26 10:44
 */

val viewModelModule = module {
    viewModel { CartViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MyViewModel(get()) }

}

val localModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().patientDao() }

}

val remoteModule = module {
    single { getOkHttpClient() }
    single { createWebService<Api>(get(), Constant.BASE_URL) }
}

val repositoryModule = module {
    single { Repository(get(), get()) }
}

val appModule = listOf(viewModelModule, localModule, remoteModule, repositoryModule)