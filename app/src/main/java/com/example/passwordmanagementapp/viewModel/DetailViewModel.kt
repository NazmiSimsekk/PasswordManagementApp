package com.example.passwordmanagementapp.viewModel

import android.content.ClipData
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.model.PassModel
import com.example.passwordmanagementapp.view.DetailFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailViewModel: ViewModel() {

    var detailPlatformName = MutableLiveData<String>()
    var detailEmail = MutableLiveData<String>()
    var detailUserName = MutableLiveData<String>()
    var detailPassword = MutableLiveData<String>()

    var detailPassList = MutableLiveData<List<PassModel>>()
    private var passList = mutableListOf<PassModel>()

    private val detailAuth = FirebaseAuth.getInstance()
    private val detailFireStore = FirebaseFirestore.getInstance()

     fun saveData(getPlatform: String, getEmail: String, getUserName: String, getPassword: String, context: Context) {

         if (getPlatform.isEmpty() || getEmail.isEmpty() || getUserName.isEmpty() || getPassword.isEmpty()){
             Toast.makeText(context, "Lütfen Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
         }else{
             val userData = hashMapOf(
                 "platformName" to getPlatform,
                 "userName" to getUserName,
                 "email" to getEmail,
                 "password" to getPassword
             )

             viewModelScope.launch(Dispatchers.IO) {
                 try {
                     detailFireStore.collection(detailAuth.currentUser!!.email!!).add(userData).await()
                     withContext(Dispatchers.Main){
                         Toast.makeText(context, "Kayıt Başarılı", Toast.LENGTH_LONG).show()
                     }
                 }catch (e:Exception){
                     withContext(Dispatchers.Main){
                         Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                     }
                 }
             }
         }
     }

    fun getData(positionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val documents = detailFireStore.collection(detailAuth.currentUser!!.email!!).document(positionId).get().await()

                documents?.let {
                    val platformName = documents.get("platformName") as String
                    val email = documents.get("email") as String
                    val userName = documents.get("userName") as String
                    val password = documents.get("password") as String

                    val passModel = PassModel(platformName, email, userName, password, positionId)
                    passList.add(passModel)

                    withContext(Dispatchers.Main) {
                        // LiveData değerlerini güncelleme
                        detailPlatformName.value = platformName
                        detailEmail.value = email
                        detailUserName.value = userName
                        detailPassword.value = password
                        detailPassList.value = passList
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun updateData(positionId: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (detailPlatformName.value == null || detailEmail.value == null || detailUserName.value == null || detailPassword.value == null){
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
                }
            }else{
                try {
                    detailFireStore.collection(detailAuth.currentUser!!.email!!).document(positionId).update("platformName", detailPlatformName.value.toString(), "email", detailEmail.value.toString()
                        , "userName", detailUserName.value.toString(), "password", detailPassword.value.toString()).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Güncelleme Başarılı", Toast.LENGTH_LONG).show()
                    }
                }catch (e:Exception){
                    println(e.message)
                }
            }
        }
    }

    fun deleteData(positionId: String, context: Context, view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                detailFireStore.collection(detailAuth.currentUser!!.email!!).document(positionId).delete().await()
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Silme Başarılı", Toast.LENGTH_LONG).show()
                    val action = DetailFragmentDirections.actionDetailFragmentToListFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }catch (e:Exception){
                println(e.message)
            }
        }
    }

    fun copyPassword(context: Context) {
        if (detailPassword.value == null){
            Toast.makeText(context, "Şifre Alınamadı", Toast.LENGTH_LONG).show()
        }else{
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("password", detailPassword.value)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Şifre Kopyalandı", Toast.LENGTH_LONG).show()
        }
    }
}