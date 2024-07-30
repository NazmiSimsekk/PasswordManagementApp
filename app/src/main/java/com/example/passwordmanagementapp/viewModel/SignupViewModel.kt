package com.example.passwordmanagementapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SignupViewModel: ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun signUpViewClick(getUsername: String) {

        _userName.value = getUsername
        println(getUsername)
    }
}