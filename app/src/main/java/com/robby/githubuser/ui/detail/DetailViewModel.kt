package com.robby.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robby.githubuser.api.model.User

class DetailViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>> = _followers

    private val _followings = MutableLiveData<List<User>>()
    val followings: LiveData<List<User>> = _followings

    fun getUserFollow(username: String) {

    }
}