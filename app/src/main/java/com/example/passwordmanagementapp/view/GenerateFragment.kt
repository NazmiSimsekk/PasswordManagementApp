package com.example.passwordmanagementapp.view

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentGenerateBinding
import com.example.passwordmanagementapp.util.GenerateClickListener
import com.example.passwordmanagementapp.viewModel.GenerateViewModel
import com.google.firebase.auth.FirebaseAuth

class GenerateFragment : Fragment(), GenerateClickListener{

    private lateinit var dataBinding: FragmentGenerateBinding
    private lateinit var viewModel: GenerateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generate, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GenerateViewModel::class.java]

        dataBinding.clickListener = this
        dataBinding.viewModel = viewModel

        viewModel.lowerCase.value = false
        viewModel.upperCase.value = false
        viewModel.numbers.value = false
        viewModel.symbols.value = false

        dataBinding.lowerCaseCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.lowerCase.value = isChecked
        }
        dataBinding.upperCaseCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.upperCase.value = isChecked
        }
        dataBinding.numbersCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.numbers.value = isChecked
        }
        dataBinding.specialCharactersCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.symbols.value = isChecked
        }
        dataBinding.generateLengthEditText.addTextChangedListener{
            viewModel.lenght.value = it.toString()
        }

        dataBinding.generateLinearLayout.setOnClickListener {
            dataBinding.generateLengthEditText.clearFocus()
            hideKeyboard(it)
        }

        dataBinding.generateLinearLayout.setOnClickListener {
            dataBinding.generateLengthEditText.clearFocus()
            hideKeyboard(it)
        }

        val currentUi = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentUi ==  Configuration.UI_MODE_NIGHT_YES){
            dataBinding.imageSignOutButton.setImageResource(R.drawable.add)
        }else{
            dataBinding.imageSignOutButton.setImageResource(R.drawable.add_black)
        }

        dataBinding.generateSignOutButton.setOnClickListener {
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
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    val action = ListFragmentDirections.actionListFragmentToLoginFragment()
                    Navigation.findNavController(it).navigate(action)
                }
            })
            alertDialog.create()
            alertDialog.show()
        }
    }

    private fun hideKeyboard(v: View){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun generatePassword(v: View) {
        viewModel.generatePassword(requireContext())
        viewModel.char.observe(viewLifecycleOwner){
            dataBinding.generatePasswordText.text = it
        }
    }

    override fun copyGeneratePassword(v: View) {
        viewModel.copyPassword(requireContext())
    }

    override fun goToList(v: View) {
        val action = GenerateFragmentDirections.actionGenerateFragmentToListFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun goToHashing(v: View) {
        val action = GenerateFragmentDirections.actionGenerateFragmentToHashingFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun goToAdd(v: View) {
        val action = GenerateFragmentDirections.actionGenerateFragmentToDetailFragment("addButtonControl", null)
        Navigation.findNavController(v).navigate(action)
    }
}