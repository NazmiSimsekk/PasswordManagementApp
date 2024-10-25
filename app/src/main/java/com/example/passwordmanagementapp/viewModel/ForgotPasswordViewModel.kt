package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ForgotPasswordViewModel: ViewModel() {

    var email = MutableLiveData<String>()

    fun resetPassword(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (email.value.isNullOrEmpty()){
                    Toast.makeText(context, "Lütfen Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
                }else{
                    val auth = FirebaseAuth.getInstance()
                    auth.sendPasswordResetEmail(email.value!!).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Şifre sıfırlama talebiniz gönderildi", Toast.LENGTH_LONG).show()
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Şifre sıfırlama talebiniz gönderildi", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}