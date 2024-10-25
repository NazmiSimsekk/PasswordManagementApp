package com.example.passwordmanagementapp.viewModel

import android.content.ClipData
import android.content.Context
import android.widget.Toast
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
    var lenght = MutableLiveData<String>()

    private val lowerList = "abcdefghijklmnopqrstuvwxyz"
    private val upperList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numberList = "0123456789"
    private val symbolList = "!@#%^&*()_-+=<>?"

    private var isValidPassword: Boolean = false

    var char = MutableLiveData<String>()
    private var charpool: String? = null

    fun generatePassword(context: Context) {

        charpool = ""

        viewModelScope.launch(Dispatchers.Default) {

            if (lenght.value != "" && lenght.value != null && lenght.value != "0" && lenght.value!!.toInt() <= 256){

                if(lowerCase.value!!) charpool += lowerList
                if(upperCase.value!!) charpool += upperList
                if(numbers.value!!) charpool += numberList
                if(symbols.value!!) charpool += symbolList


                if (lowerCase.value == false && upperCase.value == false && numbers.value == false && symbols.value == false){
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Select at least one option", Toast.LENGTH_LONG).show()
                        charpool = "0"
                    }
                }

                var password: String

                do {

                    var controlList = arrayListOf<Boolean>()

                    controlList.clear()


                    password = (1..(lenght.value?.toInt()!!)).map {
                        charpool!!.random()
                    }.joinToString("")

                    var controlLower = false
                    var controlUpper = false
                    var controlNumber = false
                    var controlSymbol = false

                    controlList.add(controlLower)
                    controlList.add(controlUpper)
                    controlList.add(controlNumber)
                    controlList.add(controlSymbol)

                    if (lowerCase.value!!){
                         controlLower = password.any { it in lowerList }
                    }else{
                        controlList.remove(controlLower)
                    }

                    if (upperCase.value!!){
                        controlUpper = password.any { it in upperList }
                    }else{
                        controlList.remove(controlUpper)
                    }

                    if (numbers.value!!){
                        controlNumber = password.any { it in numberList }
                    }else{
                        controlList.remove(controlNumber)
                    }

                    if (symbols.value!!){
                        controlSymbol = password.any { it in symbolList }
                    }else{
                        controlList.remove(controlSymbol)
                    }

                    for(i in controlList){
                        if (i){
                            isValidPassword = true
                        }else{
                            isValidPassword = false
                            break
                        }
                    }

                }while (isValidPassword)

                withContext(Dispatchers.Main){
                    char.value = password
                }
            }else{
                Toast.makeText(context, "En Fazla 256 Karakter", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun copyPassword(context: Context) {
        if (char.value == null){
            Toast.makeText(context, "Şifre Alınamadı", Toast.LENGTH_LONG).show()
        }else{
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("password", char.value)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Şifre Kopyalandı", Toast.LENGTH_LONG).show()
        }
    }
}