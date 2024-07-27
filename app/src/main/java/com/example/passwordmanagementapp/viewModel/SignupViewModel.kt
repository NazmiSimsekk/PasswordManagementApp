package com.example.passwordmanagementapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel: ViewModel() {

    var userName = MutableLiveData<String>()
    private var password = MutableLiveData<String>()
    private var tryPassword = MutableLiveData<String>()

    private var olusturulanKullanici = MutableLiveData<String>()

    init {
        olusturulanKullanici.value = userName.value.toString()

        password.value = ""
        tryPassword.value = ""
        println("Kullanıcı Adı: $olusturulanKullanici")
    }
}