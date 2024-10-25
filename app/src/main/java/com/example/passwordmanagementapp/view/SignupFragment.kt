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
import com.example.passwordmanagementapp.databinding.FragmentSignupBinding
import com.example.passwordmanagementapp.util.SignupClick
import com.example.passwordmanagementapp.viewModel.SignupViewModel

class SignupFragment : Fragment(), SignupClick {

    private lateinit var dataBinding: FragmentSignupBinding
    private var viewModel = SignupViewModel()

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

        dataBinding.signupUserNameEditText.addTextChangedListener {
            it?.let {
                viewModel.email.value = it.toString()
            }
        }

        dataBinding.signupPassEditText.addTextChangedListener {
            it?.let {
                viewModel.password.value = it.toString()
            }
        }

        dataBinding.signupTryPassEditText.addTextChangedListener {
            it?.let {
                viewModel.tryPassword.value = it.toString()
            }
        }

        dataBinding.signUpLinearLayout.setOnClickListener {
            hideKeyboard(it)
            dataBinding.signupUserNameEditText.clearFocus()
            dataBinding.signupPassEditText.clearFocus()
            dataBinding.signupTryPassEditText.clearFocus()
        }
    }

    private fun hideKeyboard(v: View){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun signUpClick(v: View) {
        viewModel.signUp(viewModel.email.value.toString(),viewModel.password.value.toString(),viewModel.tryPassword.value.toString(),requireContext(),v)
    }
}