package com.example.passwordmanagementapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentLoginBinding
import com.example.passwordmanagementapp.util.SignupClick
import com.example.passwordmanagementapp.viewModel.LoginViewModel


class LoginFragment : Fragment(), SignupClick{

    private lateinit var dataBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    private var loginEmail: String = ""
    private var loginPassword: String = ""

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
    }

    override fun signUpClick(v: View) {
        TODO("Not yet implemented")
    }


}