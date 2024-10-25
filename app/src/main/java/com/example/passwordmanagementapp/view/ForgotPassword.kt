package com.example.passwordmanagementapp.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentForgotPasswordBinding
import com.example.passwordmanagementapp.util.ForgotPasswordClickListener
import com.example.passwordmanagementapp.viewModel.ForgotPasswordViewModel

class ForgotPassword : Fragment(), ForgotPasswordClickListener {

    private lateinit var dataBinding: FragmentForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        dataBinding.viewModel = viewModel
        dataBinding.clickListener = this

        dataBinding.emailEditText.addTextChangedListener {
            it.let {
                viewModel.email.value = it.toString()
            }
        }

        dataBinding.forgotLinearLayout.setOnClickListener {
            hideKeyboard(it)
            dataBinding.emailEditText.clearFocus()
        }
    }

    private fun hideKeyboard(v: View){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun resetPassword(v: View) {
        viewModel.resetPassword(requireContext())
    }
}