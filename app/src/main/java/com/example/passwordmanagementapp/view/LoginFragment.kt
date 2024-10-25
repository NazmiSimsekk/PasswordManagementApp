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
import androidx.navigation.Navigation
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentLoginBinding
import com.example.passwordmanagementapp.util.LoginClickListener
import com.example.passwordmanagementapp.viewModel.LoginViewModel


class LoginFragment : Fragment(), LoginClickListener {

    private lateinit var dataBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.clickListener = this
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        dataBinding.loginVar = viewModel

        viewModel.getCurrentUser(view)

        dataBinding.loginUsernameEditText.addTextChangedListener {
            it?.let {
                viewModel.loginEmail.value = it.toString()
            }
        }

        dataBinding.loginPasswordEditText.addTextChangedListener {
            it?.let {
                viewModel.loginPassword.value = it.toString()
            }
        }

        dataBinding.loginLinearLayout.setOnClickListener {
            hideKeyboard(it)
            dataBinding.loginUsernameEditText.clearFocus()
            dataBinding.loginPasswordEditText.clearFocus()
        }
    }

    private fun hideKeyboard(v: View){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun loginClick(v: View) {
        viewModel.login(viewModel.loginEmail.value.toString(),viewModel.loginPassword.value.toString(), requireContext(), v)
    }

    override fun registerClick(v: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        Navigation.findNavController(v).navigate(action)
    }

    override fun forgotPasswordClick(v: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotPassword()
        Navigation.findNavController(v).navigate(action)
    }
}