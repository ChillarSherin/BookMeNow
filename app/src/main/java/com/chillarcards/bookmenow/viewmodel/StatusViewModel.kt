package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.ProfileResponseModel
import com.chillarcards.bookmenow.data.model.RegisterModel
import com.chillarcards.bookmenow.data.model.StatusResponseModel
import com.chillarcards.bookmenow.data.model.WorkResponseModel
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
class StatusViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _statusData = MutableLiveData<Resource<StatusResponseModel>?>()
    val statusData: LiveData<Resource<StatusResponseModel>?> get() = _statusData


    fun getStatus() {
        viewModelScope.launch(NonCancellable) {
            try {
                _statusData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getShopClose(
                    ).let {
                        if (it.isSuccessful) {
                            _statusData.postValue(Resource.success(it.body()))
                        } else {
                            _statusData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _statusData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _statusData.value = null
    }
}