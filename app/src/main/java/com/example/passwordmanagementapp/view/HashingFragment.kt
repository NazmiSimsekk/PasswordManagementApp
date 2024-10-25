package com.example.passwordmanagementapp.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentHashingBinding
import com.example.passwordmanagementapp.util.HashingClickListener
import com.example.passwordmanagementapp.viewModel.HashingViewModel
import com.google.firebase.auth.FirebaseAuth

class HashingFragment : Fragment(), HashingClickListener {

    private lateinit var dataBinding: FragmentHashingBinding
    private lateinit var viewModel: HashingViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_hashing, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HashingViewModel::class.java]
        dataBinding.viewModel = viewModel
        dataBinding.clickListener = this

        dataBinding.hashingEditText.addTextChangedListener {
            it.let {
                viewModel.hashingText.value = it.toString()
            }
        }

        auth = FirebaseAuth.getInstance()

        dataBinding.hashinSignOut.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle("Çıkış Yap")
            alertDialog.setMessage("Çıkış yapmak istediğinize emin misiniz?")
            alertDialog.setNegativeButton("Hayır", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
            alertDialog.setPositiveButton("Evet", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    auth.signOut()
                    val action = ListFragmentDirections.actionListFragmentToLoginFragment()
                    Navigation.findNavController(it).navigate(action)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }

        dataBinding.hashingLinearLayout.setOnClickListener {
            dataBinding.hashingEditText.clearFocus()
            hideKeyboard(it)
        }

        val currentUi = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentUi ==  Configuration.UI_MODE_NIGHT_YES){
            dataBinding.imageAddButton.setImageResource(R.drawable.add)
        }else{
            dataBinding.imageAddButton.setImageResource(R.drawable.add_black)
        }
    }

    override fun hashingClickListener(v: View) {
        viewModel.hashing(requireContext())
        dataBinding.hashingTextView.text = viewModel.hashingResult.value
    }

    override fun hashResultCopy(v: View) {
        val textToCopy = dataBinding.hashingTextView.text

        // ClipboardManager'ı al
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Kopyalanacak veriyi tanımla
        val clip = ClipData.newPlainText("label", textToCopy)

        // Veriyi kopyala
        clipboard.setPrimaryClip(clip)

        // Kullanıcıya bilgi ver (isteğe bağlı)
        Toast.makeText(requireContext(), "Metin kopyalandı!", Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(v: View){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun goToList(v: View) {
        val action = HashingFragmentDirections.actionHashingFragmentToListFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun gotToGenerate(v: View) {
        val action = HashingFragmentDirections.actionHashingFragmentToGenerateFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun goToAddPassword(v: View) {
        val action = HashingFragmentDirections.actionHashingFragmentToDetailFragment("add", null)
        Navigation.findNavController(v).navigate(action)
    }
}