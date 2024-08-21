package com.example.passwordmanagementapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentGenerateBinding
import com.example.passwordmanagementapp.util.GenerateClickListener
import com.example.passwordmanagementapp.viewModel.GenerateViewModel

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
            viewModel.lenght.value = it.toString().toInt()
        }
    }

    override fun generatePassword(v: View) {
        viewModel.generatePassword(requireContext(), viewLifecycleOwner)
        viewModel.char.observe(viewLifecycleOwner){
            dataBinding.generatePasswordText.text = it
        }
    }

    override fun copyGeneratePassword(v: View) {
        TODO("Not yet implemented")
    }


}