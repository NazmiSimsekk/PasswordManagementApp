package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.security.MessageDigest

class HashingViewModel: ViewModel() {

    var hashingText = MutableLiveData<String>()
    var hashingResult = MutableLiveData<String>()

    fun hashing(context: Context){

        if (hashingText.value == "" || hashingText.value == null){
            Toast.makeText(context, "Lütfen Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
        }else{
            val bytes = hashingText.value!!.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            hashingResult.value = digest.fold("") { str, it -> str + "%02x".format(it)}.toString()
        }
    }
}