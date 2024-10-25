package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.view.LoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel: ViewModel(){

    val loginAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var loginEmail = MutableLiveData<String>()
    var loginPassword = MutableLiveData<String>()

    private var currentUser = loginAuth.currentUser
    var control = MutableLiveData<String>()

    fun login(getEmail: String, getPassword: String,context: Context, v: View) {


        if (getEmail.isEmpty() || getPassword.isEmpty()) {
           Toast.makeText(context, "Lütfen Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loginAuth.signInWithEmailAndPassword(getEmail,getPassword).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Giriş Başarılı", Toast.LENGTH_LONG).show()
                        control.value = "login"
                        val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                        Navigation.findNavController(v).navigate(action)
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun getCurrentUser(v: View) {
        if (currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
            Navigation.findNavController(v).navigate(action)
        }
    }

}