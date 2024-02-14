package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.BookingResponseModel
import com.chillarcards.bookmenow.data.model.ProfileResponseModel
import com.chillarcards.bookmenow.data.model.StatusResponseModel
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
class BookingViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _bookingData = MutableLiveData<Resource<BookingResponseModel>?>()
    val bookingData: LiveData<Resource<BookingResponseModel>?> get() = _bookingData
    private val _bookUpdateData = MutableLiveData<Resource<StatusResponseModel>?>()
    val bookStatusData: LiveData<Resource<StatusResponseModel>?> get() = _bookUpdateData

    var doctorID = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var bookingId = MutableLiveData<String>()

    fun getBookingList() {
        viewModelScope.launch(NonCancellable) {
            try {
                _bookingData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getBookigDetails(
                        doctorID.value.toString(),
                        date.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _bookingData.postValue(Resource.success(it.body()))
                        } else {
                            Log.e("abc_otp", "verifyProfile 5: "+it.message().toString())
                            _bookingData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _bookingData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun getConfirmBooking() {
        viewModelScope.launch(NonCancellable) {
            try {
                _bookUpdateData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getUpdate(
                        bookingId.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _bookUpdateData.postValue(Resource.success(it.body()))
                        } else {
                            Log.e("abc_otp", "verifyProfile 5: "+it.message().toString())
                            _bookUpdateData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _bookUpdateData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _bookingData.value = null
        _bookUpdateData.value = null
    }
}