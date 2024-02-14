package com.chillarcards.bookmenow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chillarcards.bookmenow.data.model.ProfileResponseModel
import com.chillarcards.bookmenow.data.model.RegisterModel
import com.chillarcards.bookmenow.data.repository.AuthRepository
import com.chillarcards.bookmenow.utills.Const
import com.chillarcards.bookmenow.utills.NetworkHelper
import com.chillarcards.bookmenow.utills.Resource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @Author: Sherin
 * @Date: 07-02-2024$
 * Chillar
 */
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val _profileData = MutableLiveData<Resource<ProfileResponseModel>?>()
    val profileData: LiveData<Resource<ProfileResponseModel>?> get() = _profileData

    var mob = MutableLiveData<String>()

    fun getProfile() {
        viewModelScope.launch(NonCancellable) {
            try {
                _profileData.postValue(Resource.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    authRepository.getProfile(
                        mob.value.toString()
                    ).let {
                        if (it.isSuccessful) {
                            _profileData.postValue(Resource.success(it.body()))
                        } else {
                            Log.e("abc_otp", "verifyProfile 5: "+it.message().toString())
                            _profileData.postValue(Resource.error(it.errorBody().toString(), null))
                        }
                    }
                } else {
                    _profileData.postValue(Resource.error("No Internet Connection", null))
                }
            } catch (e: Exception) {
                Log.e("abc_otp", "verifyOTP: ", e)
            }
        }
    }

    fun clear() {
        _profileData.value = null
    }
}