package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.ProfileResponseModel
import com.chillarcards.bookmenow.data.model.RegisterModel
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
class WorkViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _workData = MutableLiveData<Resource<WorkResponseModel>?>()
    val workData: LiveData<Resource<WorkResponseModel>?> get() = _workData

    var doctorID = MutableLiveData<String>()

    fun getWork() {
        viewModelScope.launch(NonCancellable) {
            try {
                _workData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getWork(
                        doctorID.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _workData.postValue(Resource.success(it.body()))
                        } else {
                            _workData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _workData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _workData.value = null
    }
}