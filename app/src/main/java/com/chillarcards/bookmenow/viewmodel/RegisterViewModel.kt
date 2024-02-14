package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.RegisterModel
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
class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _regData = MutableLiveData<Resource<RegisterModel>?>()
    val regData: LiveData<Resource<RegisterModel>?> get() = _regData
    private val _tokenData = MutableLiveData<Resource<RegisterModel>?>()
    val tokenData: LiveData<Resource<RegisterModel>?> get() = _tokenData

    var mob = MutableLiveData<String>()

    fun verifyMobile() {
        viewModelScope.launch(NonCancellable) {
            try {
                _regData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.verifyMobile(
                        mob.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _regData.postValue(Resource.success(it.body()))
                        } else {
                            _regData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _regData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun getAuthToken() {
        viewModelScope.launch(NonCancellable) {
            try {
                _tokenData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.verifyMobile(
                        mob.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _tokenData.postValue(Resource.success(it.body()))
                        } else {
                            _tokenData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _tokenData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _regData.value = null
    }
}