package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.BookingReportResponseModel
import com.chillarcards.bookmenow.data.repository.AuthRepository
import com.chillarcards.bookmenow.utills.NetworkHelper
import com.chillarcards.bookmenow.utills.Resource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

/**
 * @Author: Sherin
 * @Date: 07-02-2024$
 * Chillar
 */
class ReportViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _reportData = MutableLiveData<Resource<BookingReportResponseModel>?>()
    val reportData: LiveData<Resource<BookingReportResponseModel>?> get() = _reportData

    var doctorID = MutableLiveData<String>()
    var date = MutableLiveData<String>()

    fun getReport() {
        viewModelScope.launch(NonCancellable) {
            try {
                _reportData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getReport(
                        doctorID.value.toString(),
                        date.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _reportData.postValue(Resource.success(it.body()))
                        } else {
                            Log.e("abc_otp", "verifyProfile 5: "+it.message().toString())
                            _reportData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _reportData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _reportData.value = null
    }
}