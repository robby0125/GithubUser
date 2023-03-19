package com.robby.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robby.githubuser.api.ApiConfig
import com.robby.githubuser.api.ApiResponse
import com.robby.githubuser.api.model.ListUserResponse
import com.robby.githubuser.api.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUserResponse = MutableLiveData<ApiResponse<List<User>>>()
    val listUserResponse: LiveData<ApiResponse<List<User>>> = _listUserResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val defaultMessage = "Failed to load data!"

    init {
        getListUser()
    }

    fun getListUser(query: String = "robby") {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListUser(query)
        client.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val listUser = response.body()?.items

                    if (listUser?.isNotEmpty() == true)
                        _listUserResponse.value = ApiResponse.success(listUser)
                    else
                        _listUserResponse.value = ApiResponse.empty("Nothing to show!", emptyList())
                } else {
                    val message = response.message().ifEmpty { defaultMessage }
                    _listUserResponse.value = ApiResponse.error(message, emptyList())
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                _isLoading.value = false
                _listUserResponse.value =
                    ApiResponse.error(t.message ?: defaultMessage, emptyList())
            }
        })
    }
}