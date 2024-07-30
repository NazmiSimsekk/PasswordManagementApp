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
    private var nazmi: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]
        return  dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.signUpClick = this
        dataBinding.signUpVar = viewModel

        viewModel.userName.observe(viewLifecycleOwner){
            dataBinding.userNameEditText.setText(it)
        }

    }

    override fun signUpClick(v: View) {
        nazmi = dataBinding.userNameEditText.text.toString()
        viewModel.signUpViewClick(nazmi!!)

    }
}