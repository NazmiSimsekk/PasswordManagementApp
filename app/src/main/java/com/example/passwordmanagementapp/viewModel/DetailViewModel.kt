package com.example.passwordmanagementapp.viewModel

import android.content.ClipData
import android.content.Context
import android.util.Base64
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
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DetailViewModel: ViewModel() {

    var detailPlatformName = MutableLiveData<String>()
    var detailEmail = MutableLiveData<String>()
    var detailUserName = MutableLiveData<String>()
    var detailPassword = MutableLiveData<String>()
    var detailWebSite = MutableLiveData<String>()

    var detailPassList = MutableLiveData<List<PassModel>>()
    private var passList = mutableListOf<PassModel>()

    private val detailAuth = FirebaseAuth.getInstance()
    private val detailFireStore = FirebaseFirestore.getInstance()

    /*private val base64Key = "MnbhmZHPxCtFGWQNRalnF740eG6AuxqnORtdY7iKGoA="
    private val base64Iv = "Z/td6ds4aXxBb/YeOLuu6Q=="

     */
    private val base64Key = "21YFQOLCQDeECkaACgNPiNtTKdPb1/IegJE6Vy3xc+c="
    private val base64Iv = "05Lj0RYb+4FUrNYgU+wTdQ=="

    private val secretKey: SecretKey = SecretKeySpec(Base64.decode(base64Key,Base64.DEFAULT), "AES")
    private val ivSpec: IvParameterSpec = IvParameterSpec(Base64.decode(base64Iv, Base64.DEFAULT))

     fun saveData(getPlatform: String, getEmail: String, getUserName: String, getPassword: String, getWebSite: String, context: Context) {

         if (getPlatform.isEmpty() || getEmail.isEmpty() || getUserName.isEmpty() || getPassword.isEmpty()){
             Toast.makeText(context, "Lütfen Boş Alan Bırakmayınız", Toast.LENGTH_LONG).show()
         }else{

             val encryptedPassword = encrptedData(getPassword, secretKey, ivSpec)
             val encryptedEmail = encrptedData(getEmail, secretKey, ivSpec)
             val encryptedUserName = encrptedData(getUserName, secretKey, ivSpec)
             val encryptedWebSite = encrptedData(getWebSite, secretKey, ivSpec)

             val newDocument = detailFireStore.collection(detailAuth.currentUser!!.email!!).document()
             val newDocumentId = newDocument.id

             val encryptedPasswordId = encrptedData(newDocumentId, secretKey, ivSpec)

             val userData = hashMapOf(
                 "PlatformName" to getPlatform,
                 "UserName" to encryptedUserName,
                 "Email" to encryptedEmail,
                 "Password" to encryptedPassword,
                 "Website" to encryptedWebSite,
                 "PasswordID" to encryptedPasswordId
             )

             viewModelScope.launch(Dispatchers.IO) {
                 try {

                     newDocument.set(userData)
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
                    val platformName = documents.get("PlatformName") as String
                    val email = documents.get("Email") as String
                    val userName = documents.get("UserName") as String
                    val password = documents.get("Password") as String
                    val webSite = documents.get("Website") as String


                    val decryptedPassword = decryptedData(password, secretKey, ivSpec)
                    val decryptedEmail = decryptedData(email, secretKey, ivSpec)
                    val decryptedUserName = decryptedData(userName, secretKey, ivSpec)
                    val decryptedWebSite = decryptedData(webSite, secretKey, ivSpec)

                    val passModel = PassModel(platformName, decryptedEmail, decryptedUserName, decryptedPassword, positionId, decryptedWebSite)
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

                    val platformName = detailPlatformName.value.toString()
                    val email = detailEmail.value.toString()
                    val userName = detailUserName.value.toString()
                    val password = detailPassword.value.toString()
                    val webSite = detailWebSite.value.toString()


                    val encryptedPassword = encrptedData(password, secretKey, ivSpec)
                    val encryptedEmail = encrptedData(email, secretKey, ivSpec)
                    val encryptedUserName = encrptedData(userName, secretKey, ivSpec)
                    val encryptedWebSite = encrptedData(webSite, secretKey, ivSpec)


                    detailFireStore.collection(detailAuth.currentUser!!.email!!).document(positionId).update("PlatformName", platformName  , "Email", encryptedEmail
                        , "UserName", encryptedUserName, "Password", encryptedPassword, "WebSite", encryptedWebSite).await()
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

    private fun encrptedData(data: String, secretKey: SecretKey, iv: IvParameterSpec): String{
        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        }catch (e: Exception){
            println("Şifreleme hatası ${e.message}")
            ""
        }
    }

    private fun decryptedData(encryptedData: String, secretKey: SecretKey, iv: IvParameterSpec): String{
        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
            val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            String(decryptedBytes)
        }catch (e: Exception){
            println("Şifre çözme hatası ${e.message}")
            ""
        }
    }
}