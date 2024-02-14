package com.chillarcards.bookmenow.di.module

import com.chillarcards.bookmenow.viewmodel.GeneralViewModel
import com.chillarcards.bookmenow.viewmodel.ProfileViewModel
import com.chillarcards.bookmenow.viewmodel.RegisterViewModel
import com.chillarcards.bookmenow.viewmodel.WorkViewModel
import com.chillarcards.bookmenow.viewmodel.*
import org.koin.dsl.module


/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */
val viewModelModule = module {
    single {
        RegisterViewModel(get(), get())
    }
    single {
        ProfileViewModel(get(), get())
    }
    single {
        WorkViewModel(get(), get())
    }
    single {
        GeneralViewModel(get(), get())
    }
    single {
        BankViewModel(get(), get())
    }
    single {
        BookingViewModel(get(), get())
    }
    single {
        ReportViewModel(get(), get())
    }
    single {
        StatusViewModel(get(), get())
    }
}