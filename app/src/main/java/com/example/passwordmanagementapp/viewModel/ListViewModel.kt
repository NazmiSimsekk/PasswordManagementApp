package com.example.passwordmanagementapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanagementapp.model.PassModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ListViewModel: ViewModel() {

    val passList = MutableLiveData<List<PassModel>>()
    private var db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    private var platformList = mutableListOf<PassModel>()

    fun getData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val documents = db.collection(auth.currentUser!!.email!!).get().await()

                // Platform listesini yeni verilerle güncellemeden önce temizliyoruz
                platformList.clear()

                // Snapshot içindeki dökümanları döngü ile dolaşıyoruz
                for (document in documents.documents) {
                    val platformName = document.getString("platformName") ?: ""
                    val email = document.getString("email") ?: ""
                    val userName = document.getString("userName") ?: ""
                    val password = document.getString("password") ?: ""
                    val id = document.id

                    val passModel = PassModel(platformName, email, userName, password, id)
                    platformList.add(passModel)
                }

                // Yeni listeyi LiveData ile güncelliyoruz
                withContext(Dispatchers.Main) {
                    passList.value = platformList
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}