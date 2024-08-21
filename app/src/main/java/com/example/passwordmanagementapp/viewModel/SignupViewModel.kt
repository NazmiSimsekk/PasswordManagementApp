package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.view.SignupFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SignupViewModel: ViewModel() {

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var tryPassword = MutableLiveData<String>()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String, tryPassword: String, context: Context, view: View){

        if (email.isEmpty() || password.isEmpty() || tryPassword.isEmpty()){
            Toast.makeText(context, "Lütfen Boş Alanları doldurunuz", Toast.LENGTH_LONG).show()
        }else if(password != tryPassword){
            Toast.makeText(context, "Girilen Şifreler Aynı Değil", Toast.LENGTH_LONG).show()
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    auth.createUserWithEmailAndPassword(email,password).await()
                    auth.signOut()
                    withContext(Dispatchers.Main){
                        val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}