package com.example.passwordmanagementapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.FragmentSignupBinding
import com.example.passwordmanagementapp.util.SignupClick
import com.example.passwordmanagementapp.viewModel.SignupViewModel

class SignupFragment : Fragment(), SignupClick {

    private lateinit var dataBinding: FragmentSignupBinding
    private var viewModel = SignupViewModel()

    private var email = ""
    private var passWord = ""
    private var tryPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return  dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this@SignupFragment)[SignupViewModel::class.java]
        dataBinding.signUpClick = this@SignupFragment
        dataBinding.signUpVar = viewModel
        dataBinding.lifecycleOwner = this@SignupFragment

        viewModel.email.observe(viewLifecycleOwner){
            it?.let {
                dataBinding.signupUserNameEditText.setText(it)
            }
        }

        viewModel.password.observe(viewLifecycleOwner){
            it?.let {
                dataBinding.signupPassEditText.setText(it)
            }
        }

        viewModel.tryPassword.observe(viewLifecycleOwner){
            it?.let {
                dataBinding.signupTryPassEditText.setText(it)
            }
        }
    }

    override fun signUpClick(v: View) {
        email = dataBinding.signupUserNameEditText.text.toString()
        passWord = dataBinding.signupPassEditText.text.toString()
        tryPassword = dataBinding.signupTryPassEditText.text.toString()
        viewModel.signUp(email,passWord,tryPassword,requireContext(),v)
    }

    override fun onResume() {
        super.onResume()
        println("Fragment Resume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Fragment View Dead")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Fragment Dead")
    }
}