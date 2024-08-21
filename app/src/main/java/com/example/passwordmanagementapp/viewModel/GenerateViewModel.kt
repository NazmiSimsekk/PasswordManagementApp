package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenerateViewModel: ViewModel() {

    var lowerCase = MutableLiveData<Boolean>()
    var upperCase = MutableLiveData<Boolean>()
    var numbers = MutableLiveData<Boolean>()
    var symbols = MutableLiveData<Boolean>()
    var lenght = MutableLiveData<Int>()

    var char = MutableLiveData<String>()
    private var charpool: String? = null

    fun generatePassword(context: Context, lifecycleOwner: LifecycleOwner) {

        val lowerList = "abcdefghijklmnopqrstuvwxyz"
        val upperList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val numberList = "0123456789"
        val symbolList = "!@#%^&*()_-+=<>?"

        viewModelScope.launch(Dispatchers.Default) {
            var i = 0
            var isValidPassword: Boolean
            if (lenght.value != null){

                if(lowerCase.value!!) charpool += lowerList else charpool = charpool?.replace(lowerList, "")
                if(upperCase.value!!) charpool += upperList else charpool = charpool?.replace(upperList, "")
                if(numbers.value!!) charpool += numberList else charpool = charpool?.replace(numberList, "")
                if(symbols.value!!) charpool += symbolList else charpool = charpool?.replace(symbolList, "")


                if (lowerCase.value == null && upperCase.value == null && numbers.value == null && symbols.value == null){
                    Toast.makeText(context, "Select at least one option", Toast.LENGTH_LONG).show()
                }

                var password: String

                do {
                    password = (1..lenght.value!!).map {
                        charpool!!.random()
                    }.joinToString("")

                    val controlLower = !lowerCase.value!! || password.any { it in lowerList }
                    val controlUpper = !upperCase.value!! || password.any { it in upperList }
                    val controlNumber = !numbers.value!! || password.any { it in numberList }
                    val controlSymbol = !symbols.value!! || password.any { it in symbolList }

                    isValidPassword = controlLower && controlUpper && controlNumber && controlSymbol


                }while (!isValidPassword)

                withContext(Dispatchers.Main){
                    char.value = password
                }
            }
        }
    }
}