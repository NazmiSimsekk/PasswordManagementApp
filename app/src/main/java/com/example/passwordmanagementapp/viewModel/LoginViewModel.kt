package com.example.passwordmanagementapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel(){

    val loginAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var loginEmail = MutableLiveData<String>()
    var loginPassword = MutableLiveData<String>()

}